package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

    private final String GET_METHOD = "GET";
    private final String POST_METHOD = "POST";
    private final String DELETE_METHOD = "DELETE";
    private final String PATH_FOR_READ_ALL_POSTS = "/api/posts";
    private final String PATH_FOR_SAVE_POST = "/api/posts";
    private final String REGEX_FOR_GET_AND_REMOVE_POST = "/api/posts/\\d+";
    private final String SLASH = "/";
    private PostController controller;
    private PostRepository repository;
    private PostService service;

    @Override
    public void init() {
        repository = new PostRepository();
        service = new PostService(repository);
        controller = new PostController(service);
    }

    private long getId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf(SLASH) + 1));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // it is enough if project is deployed to root (/) context
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            final long id;

            System.out.println("path = " + path);
            System.out.println("method = " + method);
            // primitive routing
            if (method.equals(GET_METHOD) && path.equals(PATH_FOR_READ_ALL_POSTS)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET_METHOD) && path.matches(REGEX_FOR_GET_AND_REMOVE_POST)) {
                // easy way
                id = getId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST_METHOD) && path.equals(PATH_FOR_SAVE_POST)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE_METHOD) && path.matches(REGEX_FOR_GET_AND_REMOVE_POST)) {
                // easy way
                id = getId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}