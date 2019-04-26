import React from 'react';
import Graph from 'react-graph-vis';
import {getAllDependencies, getDeclaredClasses, getDeclaredClass} from "../api-dependency";
import '../style.css';
import isEqual from 'lodash/isEqual';

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
        console.log("v1.21");
        async function generateDependencyList(component, project) {
            if (component.dependencies !== undefined) {
                let deps = component.dependencies.split(';');
                let new_deps = deps.map(async (dep) => {
                    let valuesReturned = await getDeclaredClasses(project);
                    for (let i = 0; i < valuesReturned.length; i++) {
                        if (valuesReturned[i].declared_classes) {
                            let declaredClasses = valuesReturned[i].declared_classes.split(';');
                            if (declaredClasses.includes(dep)) {
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
            return edgeList;
        }

        getAllDependencies(this.props.project).then((valuesReturned) => {
            let nodes = this.state.graph.nodes.slice();
            let edges = this.state.graph.edges.slice();
            valuesReturned.forEach(async function(component) {
                console.log(component);
                nodes = nodes.concat([{
                    id: component.componentKey,
                    label: component.name
                }]);
                edges = edges.concat(await generateEdgeList(component.componentKey, await generateDependencyList(component, this.props.project)))
            });
            console.log(isEqual(edges,this.state.graph.edges));
            console.log(edges);
            console.log(nodes);
            this.setState({
                graph: {
                    nodes: nodes,
                    edges: edges
                },
                config: this.state.config
            });
            console.log("state set");
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