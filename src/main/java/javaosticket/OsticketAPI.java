package javaosticket;

import dto.TicketDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OsticketAPI {

    private static String url = "http://192.168.100.22/osTicket-1.17.2/api/tickets.json";
    private static String apiKey = "27D4EFCD143D5525DD149F45A2382D30";

    public static void createTicket(TicketDTO ticket) throws Exception {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create(); //Remover caracteres especiales  
        String json = gson.toJson(ticket);
        json=json.replace("\\u003d", "=");
        System.out.println(json);
        if(!ticket.isSendTicket()){
            return;
        }
        // codifica los parámetros de la solicitud
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10,TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("X-API-Key", apiKey)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        System.out.println(res);
    }
}
