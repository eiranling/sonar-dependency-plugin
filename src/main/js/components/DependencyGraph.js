import React from 'react';
import Graph from 'react-graph-vis';
import {getAllDependencies, getDeclaredClasses, getDeclaredClass} from "../api-dependency";
import '../style.css'

export default class DependencyGraph extends React.PureComponent {

    constructor(props) {
        super(props);
        this.state = {
            graph: {
                nodes: [
                    {id: "ControllerId", label: "Controller"},
                    {id: "ModelId", label: "Model"},
                    {id: "ViewId", label: "View"}
                ],
                edges: [
                    {from: "ControllerId", to: "ModelId"},
                    {from: "ViewId", to: "ControllerId"},
                    {from: "ModelId", to: "ViewId"}
                ]
            },
            config: {
                autoResize: true,
                height: '100%',
                width: '100%',
                physics: {
                    enabled: true,
                    stabilization: {
                        enabled: true,
                        iterations: 3,
                        fit: false
                    },
                    barnesHut: {
                        gravitationalConstant: -22500,
                        centralGravity: 3.65,
                        springLength: 70,
                        springConstant: 0,
                        damping: 1
                    },
                    maxVelocity: 150,
                    minVelocity: 1,
                    timestep: 0.22

                }
            }
        };
    }

    componentDidMount() {
        console.log("v1.4");
        function generateDependencyList(component) {
            if (component.dependencies !== undefined) {
                const deps = component.dependencies.split(';');
                return deps.map((dep) => {
                    getDeclaredClass(component).then((valuesReturned) => {
                        const declaredClasses = valuesReturned.declared_classes.split(';');
                        if (declaredClasses.includes(dep)) {
                            return valuesReturned.componentKey;
                        }
                        return dep;
                    });
                });
            } else {
                return [];
            }
        }

        function generateEdgeList(from, destinations) {
            let edgeList = [];
            destinations.forEach((item) => {
                edgeList = edgeList.concat([{ from: from, to: item }])
            });
            return edgeList;
        }

        getAllDependencies(this.props.project).then((valuesReturned) => {
            let state = this.state;
            valuesReturned.forEach((component) => {
               let nodes = this.state.graph.nodes.slice();
               let edges = this.state.graph.edges.slice();
               state = {
                   graph: {
                       nodes: nodes.concat([{
                           id: component.componentKey,
                           label: component.name
                       }]),
                       edges: edges.concat(generateEdgeList(component.componentKey, generateDependencyList(component)))
                   },
                   config: this.state.config
               };
            });
            return state;
        }).then((state) => {
            getDeclaredClasses(this.props.project).then((valuesReturned) => {
                let new_edges = this.state.graph.edges.slice();
                valuesReturned.forEach((component) => {
                    if (component.declared_classes !== undefined) {
                        let declared_classes = component.declared_classes.split(';');
                        let new_state = {
                            config: state.config,
                            graph: {
                                nodes: state.graph.nodes.slice(),
                                edges: new_edges.map((edge) => {
                                    if (declared_classes.includes(edge.from)) {
                                        edge.from = component.componentKey;
                                    }
                                    if (declared_classes.includes(edge.to)) {
                                        edge.to = component.componentKey;
                                    }

                                    return edge;
                                })
                            }
                        };
                        this.setState(new_state);
                    }
                });
            });

        });

    }

    render() {
        return (
            <div className="page full">
                <Graph id='dep-graph' graph={this.state.graph} options={this.state.config}/>
            </div>
        );
    };
}