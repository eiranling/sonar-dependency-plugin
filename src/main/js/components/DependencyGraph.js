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
        console.log("v1.8");
        async function generateDependencyList(component) {
            if (component.dependencies !== undefined) {
                let deps = component.dependencies.split(';');
                deps.map(async (dep) => {
                    let valuesReturned = await getDeclaredClass(component);
                    const declaredClasses = valuesReturned.declared_classes.split(';');
                    if (declaredClasses.includes(dep)) {
                        return valuesReturned.componentKey;
                    }
                    return dep;
                    });
/*                });*/
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
                let nodes = this.state.graph.nodes.slice();
                let edges = this.state.graph.edges.slice();
                valuesReturned.forEach(async (component) => {
                    nodes = nodes.concat([{
                        id: component.componentKey,
                        label: component.name
                    }]);
                    edges = edges.concat(generateEdgeList(component.componentKey, await generateDependencyList(component)))
                });
                this.setState({
                    graph: {
                        nodes: nodes,
                        edges: edges
                    },
                    config: this.state.config
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