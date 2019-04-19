import {getJSON} from 'sonar-request';

export function getAllDependencies(project) {
    console.log(project);
    return getJSON('/api/dependencies/list', {
        baseComponentKey: project,
        p: 1,
        ps: 500
    }).then((response) => {
        return response.dependencies;
    })
}