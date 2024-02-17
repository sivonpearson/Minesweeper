public class GameState {
    public static enum GameStatus {
        Menu,
        Ingame,
        Win,
        Lose,

        //Don't remove
        Unknown
    }

    public static GameStatus gameStatus = GameStatus.Menu;

    public static GameStatus getGameStatus() {
        return gameStatus;
    }
    public static void setGameStatus(GameStatus newGameStatus) {
        gameStatus = newGameStatus;
    }
}
