import io.javalin.Javalin;
import io.javalin.websocket.WsConnectContext;
import org.json.JSONObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class App {
    // Maps username -> WebSocket session context
    private static final Map<String, WsConnectContext> sessions = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // Start Javalin on Port 7070 and allow Cross-Origin (CORS) requests from the browser
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));
        }).start(7070);

        // Define WebSocket endpoint
        app.ws("/chat/{username}", ws -> {

            // Triggered when a browser tab connects
            ws.onConnect(ctx -> {
                String username = ctx.pathParam("username");
                sessions.put(username, ctx);
                System.out.println("LOG: " + username + " is online.");
            });

            // Triggered when a user sends a text payload
            ws.onMessage(ctx -> {
                JSONObject json = new JSONObject(ctx.message());
                String target = json.getString("target");

                // Route message directly to the target recipient if online
                WsConnectContext targetSession = sessions.get(target);
                if (targetSession != null && targetSession.session.isOpen()) {
                    targetSession.send(json.toString());
                    System.out.println("LOG: Routed message from " + json.getString("sender") + " to " + target);
                } else {
                    System.out.println("LOG: Target " + target + " is offline.");
                }
            });

            // Triggered when a user closes the tab or disconnects
            ws.onClose(ctx -> {
                String username = ctx.pathParam("username");
                sessions.remove(username);
                System.out.println("LOG: " + username + " went offline.");
            });
        });
    }
}