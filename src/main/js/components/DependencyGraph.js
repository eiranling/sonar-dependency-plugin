import React from 'react';
import { render } from 'react-dom';
import Graph from 'react-graph-vis';

class DependencyGraph extends React.PureComponent {

    constructor(props) {
        super(props);
        this.state = {
            graph: [
                { }
            ],
        }
    }

    render() {
        return (
            <div id="dependencyGraph">
                <Graph graph={graph} options={options} events={events}/>
            </div>
        );
    };
}