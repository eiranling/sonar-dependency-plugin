import React from 'react';
import Graph from 'react-graph-vis';
import {getAllDependencies} from "../api-dependency";
import '../style.css'

export default class DependencyGraph extends React.PureComponent {

    constructor(props) {
        super(props);
        this.state = {
            graph: {
                nodes: [
                    {id: "Controller", label: "Controller"},
                    {id: "Model", label: "Model"},
                    {id: "View", label: "View"}
                ],
                edges: [
                    {from: "Controller", to: "Model"},
                    {from: "View", to: "Controller"},
                    {from: "Model", to: "View"}
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
                    repulsion: {
                        nodeDistance: 10,
                        centralGravity: 0,
                        springConstant: 10000,

                    },
                    barnesHut: {
                        gravitationalConstant: -500,
                        centralGravity: 0,
                        springConstant: 0,
                        damping: 0,
                        avoidOverlap: true
                    }
                }
            }
        };
    }

    componentDidMount() {
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
                edgeList.concat([{ from: from, to: item }])
            });
            return edgeList;
        }

        getAllDependencies(this.props.project).then((valuesReturned) => {
            valuesReturned.forEach((component) => {
                console.log(component);
               const nodes = this.state.graph.nodes.slice();
               const edges = this.state.graph.edges.slice();
               this.setState({
                   graph: {
                       nodes: nodes.concat([{
                           id: component.componentKey,
                           label: component.name
                       }]),
                       edges: edges.concat([generateEdgeList(component.componentKey, generateDependencyList(component.dependencies))])
                   },
                   config: this.state.config
               });
            });

        });
    }

    render() {

        function changeState() {
            const nodes = this.state.graph.nodes.slice();
            nodes.concat({id: "hello"});
            this.state.graph.nodes = nodes;
        }

        return (
            <div className="page full">
                <Graph id='dep-graph' graph={this.state.graph} options={this.state.config}/>
                <button onClick={changeState}>Refresh</button>
            </div>
        );
    };
}