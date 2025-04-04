package com.checkmate.controller;

import com.checkmate.model.User;
import com.checkmate.model.Game;
import com.checkmate.model.Board;

import com.checkmate.service.UserService;
import com.checkmate.service.GameService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;


;
/**
 * WebController class to handle web requests related to user management.
 */
@Controller
@RequestMapping("/")
public class WebController {
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    /**
     * Method to display the user creation form.
     *
     * @param model the model to add attributes to.
     * @return the name of the Thymeleaf template.
     */
    @GetMapping("/")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User()); // Empty User object for the form
        return "create-user"; // Name of the Thymeleaf template
    }
    /**
     * Method to handle the submission of the user creation form.
     *
     * @param user the user object submitted from the form.
     * @param session the HTTP session to store user data.
     * @return a redirect to the create form after submission.
     */
    
    @PostMapping("/users/create")
    public String showCreateForm(User user, HttpSession session) {
        User createdUser = userService.saveUser(user);
        session.setAttribute("user", createdUser);
        return "redirect:/home";
    }
    @GetMapping("/users/create")
    public String getCreateForm(User user, HttpSession session) {
        return "create-user";
    }
    /**
     * Method to handle the user login.
     *
     * @param user the user object submitted from the form.
     * @param session the HTTP session to store user data.
     * @return a redirect to the home page after login.
     */
    @GetMapping("/home")
    public String showHome(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "redirect:/users/create";
        }
        return "home";
    }

    /**
     * Method to display the user login form.
     *
     * @param session the HTTP session to store user data.
     * @return the name of the Thymeleaf template.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/create";
    }
    /**
     * Method to display the game page.
     * @param session the HTTP session to store game data.
     * @return the name of the Thymeleaf template.
     */
    @PostMapping("/start-game")
    public String startGame(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Game game = new Game(user.getId());
            Game createdGame = gameService.saveGame(game);
            session.setAttribute("game", createdGame);
            return "redirect:/game?userId=" + user.getId() + "&gameId=" + createdGame.getId();
        } else {
            return "redirect:/users/create";
        }
    }
    /**
     * Method to display the game page.
     *
     * @param userId the ID of the user.
     * @param model the model to add attributes to.
     * @param session the HTTP session to store game data.
     * @return the name of the Thymeleaf template.
     */
    @GetMapping("/game")
    public String showGame(
        @RequestParam(required = false) Integer userId, 
        @RequestParam(required = false) Integer gameId, 
        Model model, 
        HttpSession session
        ) {
        User user = (User) session.getAttribute("user");
        Game game = (Game) session.getAttribute("game");
        if (user == null) {
            user = userService.getUserById(userId).orElse(null);
            session.setAttribute("user", user);
        }
        if (game == null) {
            game = gameService.getGameById(gameId).orElse(null);
            session.setAttribute("game", game);
        }
        if (user != null && game != null) {
            model.addAttribute("user", user);
            model.addAttribute("game", game);
            Board board = new Board();
            model.addAttribute("board", board);
            return "game";
        } else  if (user == null) {
            return "redirect:/users/create";
        } else if (game == null) {
            return "redirect:/home";
        } else {
            return "redirect:/users/create";
        }
    }
}