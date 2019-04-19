import React from 'react';
import Graph from 'react-graph-vis';

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

    render() {

        function changeState() {
            const nodes = this.state.graph.nodes.slice();
            nodes.concat({id: "hello"});
            this.state.graph.nodes = nodes;
        }

        return (
            <div className="page">
                <Graph id='dep-graph' graph={this.state.graph} options={config}/>
                <button onClick={changeState}>Refresh</button>
            </div>
        );
    };
}