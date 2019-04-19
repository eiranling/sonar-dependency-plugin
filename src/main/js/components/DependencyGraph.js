import React from 'react';
import { render } from 'react-dom';
import Graph from 'react-d3-graph';

export default class DependencyGraph extends React.PureComponent {

    state = {
        graph: {
            nodes: [
                { id: "Controller" },
                { id: "Model" },
                { id: "View" }
            ],
            links: [
                {source: "Controller", target:"Model"},
                {source: "View", target: "Controller"},
                {source: "Model", target: "View"}
            ]
        },
        config: [
            {}
        ]
    };

    render() {
        return (
            <div className="page page-limited">
                <Graph id='dep-graph' data={this.state.graph} config={this.state.config}/>
                <button onClick={this.state.graph.nodes.concat({ id: "New"})}>Refresh</button>
            </div>
        );
    };
}