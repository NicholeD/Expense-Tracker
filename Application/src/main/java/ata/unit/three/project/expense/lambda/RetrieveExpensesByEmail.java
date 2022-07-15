package ata.unit.three.project.expense.lambda;

import ata.unit.three.project.App;
import ata.unit.three.project.expense.dynamodb.ExpenseItem;
import ata.unit.three.project.expense.service.ExpenseService;
import ata.unit.three.project.expense.service.exceptions.InvalidDataException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.ata.ExcludeFromJacocoGeneratedReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExcludeFromJacocoGeneratedReport
public class RetrieveExpensesByEmail
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    static final Logger log = LogManager.getLogger();

    @Inject
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // Logging the request json to make debugging easier.
        log.info(gson.toJson(input));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String email = input.getQueryStringParameters().get("email");

        // Your Code Here
        try {
            ExpenseService expenseService = App.expenseService();
            List<ExpenseItem> expenses = expenseService.getExpensesByEmail(email);
            String output = gson.toJson(expenses);

            return response
                    .withStatusCode(200)
                    .withBody(output);

        } catch (InvalidDataException e) {

            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.errorPayload()));
        }
    }
}
