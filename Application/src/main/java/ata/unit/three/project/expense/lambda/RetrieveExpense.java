package ata.unit.three.project.expense.lambda;

import ata.unit.three.project.App;
import ata.unit.three.project.expense.dynamodb.ExpenseItem;
import ata.unit.three.project.expense.service.ExpenseService;
import ata.unit.three.project.expense.service.exceptions.InvalidDataException;
import ata.unit.three.project.expense.service.ExpenseServiceComponent;

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
import java.util.Map;
import java.util.UUID;



@ExcludeFromJacocoGeneratedReport
public class RetrieveExpense implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger log = LogManager.getLogger();

    @Inject
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson(input));

        ExpenseService expenseService = App.expenseService();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String expenseId = input.getPathParameters().get("expenseId");

//        try {
//            UUID.fromString(expenseId);
//        } catch(IllegalArgumentException e) {
//            throw new InvalidDataException("The Id is not a valid UUID");
//        }

        try {
            ExpenseItem expense = expenseService.getExpenseById(expenseId);
            if (expense == null) {
                return response
                        .withStatusCode(404);
            }


            String output = gson.toJson(expense);

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
