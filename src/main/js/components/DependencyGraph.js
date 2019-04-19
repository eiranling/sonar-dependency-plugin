import React from 'react';
import Graph from 'react-graph-vis';
import {getAllDependencies} from "../api-dependency";

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
                    enabled: false
                }
            }
        };
    }

    componentDidMount() {
        function generateDependencyList(dependencies) {
            return dependencies.split(';');
        }

        function generateEdgeList(from, destinations) {
            let edgeList = [];
            destinations.forEach((item) => {
                edgeList.concat([{ from: from, to: item }])
            })
            return edgeList;
        }

        getAllDependencies(this.props.project).then((valuesReturned) => {
            valuesReturned.forEach((component) => {
               const nodes = this.state.graph.nodes.slice();
               const edges = this.state.graph.edges.slice();
               this.state.graph = {
                   nodes: nodes.concat([{
                       id: component.componentKey,
                       label: component.name
                   }]),
                   edges: edges.concat([generateEdgeList(component.componentKey, generateDependencyList(component.dependencies))])
               }
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
            <div className="page">
                <Graph id='dep-graph' graph={this.state.graph} options={this.state.config}/>
                <button onClick={changeState}>Refresh</button>
            </div>
        );
    };
}