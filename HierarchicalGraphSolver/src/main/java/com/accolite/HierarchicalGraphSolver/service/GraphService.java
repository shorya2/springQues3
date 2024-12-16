package com.accolite.HierarchicalGraphSolver.service;

import com.accolite.HierarchicalGraphSolver.exception.NodeNotFoundException;
import com.accolite.HierarchicalGraphSolver.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class GraphService {
    private final Graph graph = new Graph();

    // Load the graph data from JSON file using BufferedReader
    public void loadGraphFromJson() {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\ShoryaMittal\\Downloads\\HierarchicalGraphSolver\\HierarchicalGraphSolver\\src\\main\\resources\\graph-data.json"))) {
            ObjectMapper objectMapper = new ObjectMapper();
            GraphData graphData = objectMapper.readValue(reader, GraphData.class);

            // Add nodes to the graph
            graphData.getNodes().forEach(graph::addNode);

            // Add relationships between nodes
            graphData.getRelationships().forEach(relationship ->
                    graph.addRelationship(relationship.getParentId(), relationship.getChildId()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load graph data from JSON", e);
        }
    }

    public Node getNode(String nodeId) {
        Node node = graph.getNodes().get(nodeId);
        if(node == null){
            throw new NodeNotFoundException("Node with id" + nodeId +" is not present");
        }
        return node;

    }

    public List<String> findPath(String startNodeId, String endNodeId) {
        List<String> path = new ArrayList<>();
        if (!dfs(startNodeId, endNodeId, path, new HashSet<>())) {
            throw new IllegalArgumentException("No path found between the nodes.");
        }
        return path;
    }

    private boolean dfs(String current, String target, List<String> path, Set<String> visited) {
        if (visited.contains(current)) return false;
        visited.add(current);
        path.add(current);
        if (current.equals(target)) return true;

        for (String neighbor : graph.getAdjacencyList().getOrDefault(current, Collections.emptyList())) {
            if (dfs(neighbor, target, path, visited)) return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

    public int calculateDepth(String nodeId) {
        Node node = graph.getNodes().get(nodeId);
        if (node == null) throw new IllegalArgumentException("Node not found.");
        int depth = 0;
        while (node.getParentId() != null) {
            depth++;
            node = graph.getNodes().get(node.getParentId());
        }
        return depth;
    }

    public String findCommonAncestor(String nodeId1, String nodeId2) {
        Set<String> ancestors1 = getAncestors(nodeId1);
        Set<String> ancestors2 = getAncestors(nodeId2);
        for (String ancestor : ancestors1) {
            if (ancestors2.contains(ancestor)) {
                return ancestor;
            }
        }
        throw new IllegalArgumentException("No common ancestor found.");
    }

    private Set<String> getAncestors(String nodeId) {
        Set<String> ancestors = new HashSet<>();
        Node node = graph.getNodes().get(nodeId);
        while (node != null && node.getParentId() != null) {
            ancestors.add(node.getParentId());
            node = graph.getNodes().get(node.getParentId());
        }
        return ancestors;
    }
}
