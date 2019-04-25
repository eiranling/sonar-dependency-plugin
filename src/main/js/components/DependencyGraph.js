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
        console.log("v1.16");
        async function generateDependencyList(component, project) {
            if (component.dependencies !== undefined) {
                let deps = component.dependencies.split(';');
                let new_deps = deps.map(async (dep) => {
                    let valuesReturned = await getDeclaredClasses(project);
                    console.log(2);
                    for (let i = 0; i < valuesReturned.length; i++) {
                        console.log(3);
                        if (valuesReturned[i].declared_classes) {
                            let declaredClasses = valuesReturned[i].declared_classes.split(';');
                            console.log(4);
                            if (declaredClasses.includes(dep)) {
                                console.log(5);
                                return valuesReturned[i].componentKey;
                            }
                        }
                    }
                    return dep;
                });
                return await Promise.all(new_deps);
            } else {
                return [];
            }
        }

        function generateEdgeList(from, destinations) {
            let edgeList = [];
            destinations.forEach((item) => {
                edgeList = edgeList.concat([{ from: from, to: item }])
            });
            console.log(edgeList);
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
                edges = edges.concat(generateEdgeList(component.componentKey, await generateDependencyList(component, this.props.project)))
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