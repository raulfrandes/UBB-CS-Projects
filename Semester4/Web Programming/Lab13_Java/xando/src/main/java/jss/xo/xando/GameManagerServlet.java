package jss.xo.xando;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

@WebServlet("/game")
public class GameManagerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        context.setAttribute("waitingPlayers", new LinkedList<HttpSession>());
        context.setAttribute("games", new HashMap<String, String[][]>());
        context.setAttribute("playerSessions", new HashMap<String, HttpSession[]>());
        context.setAttribute("gameStatus", new HashMap<String, String>());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        ServletContext context = getServletContext();
        Queue<HttpSession> waitingPlayers = (Queue<HttpSession>) context.getAttribute("waitingPlayers");
        Map<String, String[][]> games = (Map<String, String[][]>) context.getAttribute("games");
        Map<String, HttpSession[]> playerSessions = (Map<String, HttpSession[]>) context.getAttribute("playerSessions");
        Map<String, String> gameStatus = (Map<String, String>) context.getAttribute("gameStatus");

        synchronized (waitingPlayers) {
            if (session.getAttribute("gameId") == null) {
                if (waitingPlayers.isEmpty()) {
                    waitingPlayers.add(session);
                    response.getWriter().write("<html><body>Waiting for an opponent...<meta http-equiv='refresh' content='2'></body></html>");
                    return;
                } else {
                    HttpSession opponentSession = waitingPlayers.poll();
                    String gameId = session.getId() + "_" + opponentSession.getId();
                    String[][] board = new String[3][3];
                    games.put(gameId, board);
                    playerSessions.put(gameId, new HttpSession[]{session, opponentSession});
                    session.setAttribute("gameId", gameId);
                    session.setAttribute("symbol", "X");
                    opponentSession.setAttribute("gameId", gameId);
                    opponentSession.setAttribute("symbol", "O");
                    opponentSession.setAttribute("ready", true); // Mark opponent as ready
                    session.setAttribute("ready", true); // Mark self as ready
                    gameStatus.put(gameId, "playing");
                }
            }
            
            HttpSession[] players = playerSessions.get(session.getAttribute("gameId"));
            if (players != null && players.length == 2) {
                HttpSession opponentSession = players[0].equals(session) ? players[1] : players[0];
                if (opponentSession.getAttribute("ready") == null) {
                    response.getWriter().write("<html><body>Waiting for an opponent...<meta http-equiv='refresh' content='2'></body></html>");
                    return;
                }
            }
        }

        response.sendRedirect("game.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletContext context = getServletContext();
        Map<String, String[][]> games = (Map<String, String[][]>) context.getAttribute("games");
        Map<String, String> gameStatus = (Map<String, String>) context.getAttribute("gameStatus");

        String gameId = (String) session.getAttribute("gameId");
        String symbol = (String) session.getAttribute("symbol");

        if (gameId == null || symbol == null) {
            response.sendRedirect("game");
            return;
        }

        String[][] board = games.get(gameId);
        if (board == null || !gameStatus.get(gameId).equals("playing")) {
            response.sendRedirect("game");
            return;
        }

        String cell = request.getParameter("cell");
        if (cell != null) {
            int cellIndex = Integer.parseInt(cell);
            int row = cellIndex / 3;
            int col = cellIndex % 3;

            synchronized (games) {
                if (board[row][col] == null) {
                    board[row][col] = symbol;
                    if (checkWin(board, symbol)) {
                        gameStatus.put(gameId, symbol + " wins!");
                    } else if (isBoardFull(board)) {
                        gameStatus.put(gameId, "Draw");
                    }
                }
            }
        }

        response.sendRedirect("game.jsp");
    }

    private boolean checkWin(String[][] board, String symbol) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] != null && board[i][0].equals(symbol) &&
                    board[i][1] != null && board[i][1].equals(symbol) &&
                    board[i][2] != null && board[i][2].equals(symbol)) ||
                    (board[0][i] != null && board[0][i].equals(symbol) &&
                            board[1][i] != null && board[1][i].equals(symbol) &&
                            board[2][i] != null && board[2][i].equals(symbol))) {
                return true;
            }
        }
        return (board[0][0] != null && board[0][0].equals(symbol) &&
                board[1][1] != null && board[1][1].equals(symbol) &&
                board[2][2] != null && board[2][2].equals(symbol)) ||
                (board[0][2] != null && board[0][2].equals(symbol) &&
                        board[1][1] != null && board[1][1].equals(symbol) &&
                        board[2][0] != null && board[2][0].equals(symbol));
    }

    private boolean isBoardFull(String[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
