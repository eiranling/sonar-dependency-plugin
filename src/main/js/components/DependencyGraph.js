import React from 'react';
import Graph from 'react-graph-vis';
import {getAllDependencies, getDeclaredClasses} from "../api-dependency";
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
        console.log("v1.0");
        function generateDependencyList(dependencies) {
            if (dependencies !== undefined) {
                return dependencies.split(';');
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
            valuesReturned.forEach((component) => {
               let nodes = this.state.graph.nodes.slice();
               let edges = this.state.graph.edges.slice();
               this.setState({
                   graph: {
                       nodes: nodes.concat([{
                           id: component.componentKey,
                           label: component.name
                       }]),
                       edges: edges.concat(generateEdgeList(component.componentKey, generateDependencyList(component.dependencies)))
                   },
                   config: this.state.config
               });
            });

        }).then(() => {
            getDeclaredClasses(this.props.project).then((valuesReturned) => {
                let new_edges = this.state.graph.edges.slice();
                valuesReturned.forEach((component) => {
                    if (component.declared_classes !== undefined) {
                        let declared_classes = component.declared_classes.split(';');
                        this.setState({
                            graph: {
                                nodes: this.state.graph.nodes.slice(),
                                edges: new_edges.map((edge) => {
                                    if (declared_classes.includes(edge.from)) {
                                        edge.from = component.componentKey;
                                    }
                                    if (declared_classes.includes(edge.to)) {
                                        edge.to = component.componentKey;
                                    }
                                    return edge;
                                })
                            },
                            config: this.state.config
                        });
                    }
                });
                console.log(this.state);
            });
            this.forceUpdate()
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