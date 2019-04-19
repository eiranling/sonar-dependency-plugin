import React from 'react';
import { render } from 'react-dom';
import Graph from 'react-d3-graph';

export default class DependencyGraph extends React.PureComponent {

    constructor(props) {
        super(props);
        this.state = {
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
        }
    }

    render() {
        return (
            <div id="dependencyGraph" className="page page-limited sanity-check">
                <Graph id="depGraph" data={this.state.graph} config={this.state.config}/>
            </div>
        );
    };
}