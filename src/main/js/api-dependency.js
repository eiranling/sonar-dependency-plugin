import {getJSON} from 'sonar-request';

export function getAllDependencies(project, ) {
    return getJSON('/api/dependencies/list', {
        baseComponentKey: project.key,
        p: 1,
        ps: 500,
        qualifier: "FIL"
    }).then((response) => {
        return response.dependencies;
    })
}

export function getDeclaredClasses(project) {
    return getJSON('api/declared_classes/list', {
        baseComponentKey: project.key,
        p: 1,
        ps: 500,
        qualifier: "FIL"
    }).then((response) => {
        return response.declaredClasses;
    })
}