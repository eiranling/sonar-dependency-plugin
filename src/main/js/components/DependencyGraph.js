import React from 'react';
import Graph from 'react-d3-graph';

export default class DependencyGraph extends React.PureComponent {

    constructor(props) {
        super(props);
        this.state = {
            graph: {
                nodes: [
                    {id: "Controller"},
                    {id: "Model"},
                    {id: "View"}
                ],
                links: [
                    {source: "Controller", target: "Model"},
                    {source: "View", target: "Controller"},
                    {source: "Model", target: "View"}
                ]
            },
            config: [
                {}
            ]
        };
    }

    render() {

        function changeState() {
            const nodes = this.state.graph.nodes.slice();
            nodes.concat({id: "hello"});
            this.state.graph.nodes = nodes;
        }

        return (
            <div className="page page-limited">
                <Graph id='dep-graph' data={this.state.graph} config={this.state.config}/>
                <button onClick={changeState}>Refresh</button>
            </div>
        );
    };
}