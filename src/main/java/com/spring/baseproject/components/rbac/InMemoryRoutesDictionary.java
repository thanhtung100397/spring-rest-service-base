package com.spring.baseproject.components.rbac;

import com.spring.baseproject.exceptions.rbac.EndpointNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryRoutesDictionary {
    private Path root;

    public InMemoryRoutesDictionary() {
        this.root = new Path(null);
    }

    public void addRoute(String route) {
        String[] routePaths;
        if (route.startsWith("/")) {
            routePaths = route.substring(1).split("/");
        } else {
            routePaths = route.split("/");
        }
        createChildPath(routePaths, 0, root);
    }

    private void createChildPath(String[] routePaths, int index, Path parent) {
        String name = routePaths[index];
        Path pathFound = parent.getChildPathByName(name);
        if (pathFound == null) {
            pathFound = new Path(name);
            parent.addChildPath(pathFound);
        }
        index++;
        if (index < routePaths.length) {
            createChildPath(routePaths, index, pathFound);
        }
    }

    public String getRoute(String uri) {
        try {
            StringBuilder route = new StringBuilder();
            if (uri.startsWith("/")) {
                uri = uri.substring(1);
            }
            String[] uriParts = uri.split("/");
            constructRoute(uriParts, 0, root, route);
            return route.toString();
        } catch (EndpointNotFoundException e) {
            return null;
        }
    }

    private void constructRoute(String[] uriParts, int index, Path parent, StringBuilder result) throws EndpointNotFoundException {
        if (index < uriParts.length && !parent.isEnd()) {
            Path pathFound = parent.findChildPathByName(uriParts[index]);
            if (pathFound == null) {
                throw new EndpointNotFoundException();
            } else {
                result.append("/").append(pathFound.name);
                constructRoute(uriParts, index + 1, pathFound, result);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        root.getString(result, root,0);
        return result.toString();
    }

    private class Path {
        private String name;
        private Map<String, Path> childrenMap;


        public Path(String name) {
            this.name = name;
            this.childrenMap = new HashMap<>();
        }

        public void addChildPath(Path child) {
            childrenMap.put(child.name, child);
        }

        public boolean isEnd() {
            return childrenMap.size() == 0;
        }

        public Path findChildPathByName(String childName) {
            Path pathFound = childrenMap.get(childName);
            if (pathFound == null) {
                return childrenMap.get("*");
            }
            return pathFound;
        }

        public Path getChildPathByName(String childName) {
            return childrenMap.get(childName);
        }

        public void getString(StringBuilder result, Path path, int level) {
            for (int i = 0; i < level; i++) {
                result.append("-");
            }
            result.append(path.name).append("\n");
            for (Map.Entry<String, Path> entry : path.childrenMap.entrySet()) {
                getString(result, entry.getValue(), level + 1);
            }
        }
    }
}
