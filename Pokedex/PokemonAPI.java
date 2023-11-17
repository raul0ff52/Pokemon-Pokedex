import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;


public class PokemonAPI {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://pokeapi.co/api/v2/pokemon?limit=1017"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = new JSONObject(response.body());
        JSONArray results = jsonObject.getJSONArray("results");

        FileWriter csvWriter = new FileWriter("pokemons.csv");
        csvWriter.append("ID,Name,Type,Stats\n");

        for (int i = 0; i < results.length(); i++) {
            String url = results.getJSONObject(i).getString("url");
            HttpRequest pokemonRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> pokemonResponse = client.send(pokemonRequest, HttpResponse.BodyHandlers.ofString());
            JSONObject pokemonObject = new JSONObject(pokemonResponse.body());

            int id = pokemonObject.getInt("id");
            String name = pokemonObject.getString("name");

            JSONArray types = pokemonObject.getJSONArray("types");
            String type = types.getJSONObject(0).getJSONObject("type").getString("name");

            JSONArray stats = pokemonObject.getJSONArray("stats");
int hp = 0, attack = 0, defense = 0, speed = 0;
for (int j = 0; j < stats.length(); j++) {
    JSONObject statObject = stats.getJSONObject(j);
    String statName = statObject.getJSONObject("stat").getString("name");
    int baseStat = statObject.getInt("base_stat");
    switch (statName) {
        case "hp":
            hp = baseStat;
            break;
        case "attack":
            attack = baseStat;
            break;
        case "defense":
            defense = baseStat;
            break;
        case "speed":
            speed = baseStat;
            break;
    }

}

    csvWriter.append(String.valueOf(id) + "," + name + "," + type + "," + hp + "," + attack + "," + defense + "," + speed + "\n");  
        }

        csvWriter.flush();
        csvWriter.close();
    }
}
