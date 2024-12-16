package com.accolite.HierarchicalGraphSolver.controller;

import com.accolite.HierarchicalGraphSolver.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/graph")
public class GraphController {
    @Autowired
    private GraphService graphService;

    // Endpoint to load the graph from a JSON file
    @PostMapping("/load")
    public String loadGraph() {
        graphService.loadGraphFromJson();
        return "Graph loaded successfully!";
    }

    // Endpoint to get a specific node by ID
    @GetMapping("/nodes/{id}")
    public Object getNode(@PathVariable String id) {
        return graphService.getNode(id);
    }

    // Endpoint to find the path between two nodes
    @GetMapping("/paths")
    public List<String> findPath(@RequestParam String startNodeId, @RequestParam String endNodeId) {
        return graphService.findPath(startNodeId, endNodeId);
    }

    // Endpoint to calculate the depth of a node
    @GetMapping("/nodes/{id}/depth")
    public int calculateDepth(@PathVariable String id) {
        return graphService.calculateDepth(id);
    }

    // Endpoint to find the common ancestor of two nodes
    @GetMapping("/common-ancestor")
    public String findCommonAncestor(@RequestParam String nodeId1, @RequestParam String nodeId2) {
        return graphService.findCommonAncestor(nodeId1, nodeId2);
    }
}
