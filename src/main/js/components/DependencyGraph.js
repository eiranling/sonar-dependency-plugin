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
                ],
                edges: [
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
        let project = this.props.project;
        console.log("v1.27");
        function logHeader(headername) {
            console.log("---------------------" + headername.toUpperCase() + "------------------------")
        }

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
                logHeader("deps");
                console.log(deps);
                logHeader("new_deps");
                console.log(new_deps);
                return await Promise.all(new_deps);
            } else {
                return [];
            }
        }

        function generateEdgeList(from, destinations) {
            let edgeList = [];
            logHeader("destinations");
            console.log(destinations);
            destinations.forEach((item) => {
                edgeList = edgeList.concat([{ from: from, to: item }])
            });
            return edgeList;
        }

        async function processComponents(component, nodes, edges) {
            logHeader("component");
            console.log(component);
            nodes = nodes.concat([{
                id: component.componentKey,
                label: component.name
            }]);

            let dep_list = await generateDependencyList(component, project);
            logHeader("dep_list");
            console.log(dep_list);
            let edge_list = generateEdgeList(component.componentKey, dep_list);
            logHeader("edge_list");
            console.log(edge_list);
            edges = edges.concat(edge_list)
            return {
                nodes: nodes,
                edges: edges
            }
        }

        async function asyncForEach(array, callback) {
            for (let index = 0; index < array.length; index++) {
                await callback(array[index], index, array);
            }
            return array;
        }

        async function start(thisRef) {
            let nodes = thisRef.state.graph.nodes.slice();
            let edges = thisRef.state.graph.edges.slice();

            let valuesReturned = await getAllDependencies(project);
            await asyncForEach(valuesReturned, async (value) => {
                let graph = await processComponents(value, nodes, edges);
                nodes = graph.nodes;
                edges = graph.edges;
            });
            console.log(isEqual(edges, thisRef.state.graph.edges));
            console.log(edges);
            console.log(valuesReturned);
            console.log(nodes);
            thisRef.setState({
                graph: {
                    nodes: nodes,
                    edges: edges
                },
                config: thisRef.state.config
            });
            console.log("state set");
        }

        start(this)

    }

    render() {
        return (
            <div className="page full">
                <Graph id='dep-graph' graph={this.state.graph} options={this.state.config}/>
            </div>
        );
    };
}