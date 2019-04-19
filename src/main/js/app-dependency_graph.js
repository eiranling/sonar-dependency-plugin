import React from 'react';
import { render, unmountComponentAtNode } from "react-dom";
import DependencyGraph from "./components/DependencyGraph";
import VersionsMeasuresHistoryApp from "./components/VersionsMeasuresHistoryApp";
import './style.css';

window.registerExtension("dependencies/graph", options => {

    const { el } = options;

    render(
        <VersionsMeasuresHistoryApp project={options.component} />, el
    );

    return () => unmountComponentAtNode(el);
});
