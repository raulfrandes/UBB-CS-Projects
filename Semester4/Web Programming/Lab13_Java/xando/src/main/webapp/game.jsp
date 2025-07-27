<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%
    HttpSession session1 = request.getSession();
    String gameId = (String) session1.getAttribute("gameId");
    String symbol = (String) session1.getAttribute("symbol");

    if (gameId == null || symbol == null) {
        response.sendRedirect("game");
        return;
    }

    Map<String, String[][]> games = (Map<String, String[][]>) application.getAttribute("games");
    Map<String, HttpSession[]> playerSessions = (Map<String, HttpSession[]>) application.getAttribute("playerSessions");
    Map<String, String> gameStatus = (Map<String, String>) application.getAttribute("gameStatus");

    if (games == null || playerSessions == null || gameStatus == null) {
        out.print("Error: Game data not found.");
        return;
    }

    String[][] board = games.get(gameId);
    HttpSession[] players = playerSessions.get(gameId);
    String status = gameStatus.get(gameId);
    if (players == null || players.length < 2 || players[1].getAttribute("ready") == null) {
        out.print("<html><body>Waiting for an opponent...<meta http-equiv='refresh' content='2'></body></html>");
        return;
    }

    if (status != null && !status.equals("playing")) {
        out.print("<html><body>Game Over: " + status + "<br><a href='welcome.jsp'>Start New Game</a></body></html>");
        return;
    }

    String opponentSymbol = symbol.equals("X") ? "O" : "X";
%>
<html>
<head>
    <title>Tic Tac Toe</title>
    <style>
        table { border-collapse: collapse; }
        td { width: 50px; height: 50px; text-align: center; vertical-align: middle; font-size: 24px; }
        .board { margin: 20px auto; }
    </style>
</head>
<body>
<h1>Tic Tac Toe</h1>
<form method="post" action="game">
    <table class="board" border="1">
        <%
            for (int i = 0; i < 3; i++) {
                out.print("<tr>");
                for (int j = 0; j < 3; j++) {
                    String cellValue = board[i][j];
                    int cellNumber = i * 3 + j;
                    if (cellValue == null) {
                        out.print("<td><button type='submit' name='cell' value='" + cellNumber + "'> </button></td>");
                    } else {
                        out.print("<td>" + cellValue + "</td>");
                    }
                }
                out.print("</tr>");
            }
        %>
    </table>
</form>
<script>
    setTimeout(function() {
        window.location.reload();
    }, 2000);
</script>
</body>
</html>
