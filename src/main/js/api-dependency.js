import {getJSON} from 'sonar-request';

export function getAllDependencies(project) {
    return getJSON('/api/dependencies/list', {
        baseComponentKey: project,
        p: 1,
        ps: 500
    }).then((response) => {
        console.log(response);
        return response.dependencies;
    })
}