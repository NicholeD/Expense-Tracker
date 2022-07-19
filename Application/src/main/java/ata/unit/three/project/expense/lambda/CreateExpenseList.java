package ata.unit.three.project.expense.lambda;

import ata.unit.three.project.App;
import ata.unit.three.project.expense.lambda.models.Expense;
import ata.unit.three.project.expense.lambda.models.ExpenseList;
import ata.unit.three.project.expense.service.DaggerExpenseServiceComponent;
import ata.unit.three.project.expense.service.ExpenseService;
import ata.unit.three.project.expense.service.ExpenseServiceComponent;

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

@ExcludeFromJacocoGeneratedReport
public class CreateExpenseList implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger log = LogManager.getLogger();

    @Inject
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // Logging the request json to make debugging easier.
        log.info(gson.toJson(input));

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        ExpenseServiceComponent expenseServiceComponent = DaggerExpenseServiceComponent.create();
        ExpenseService expenseService = expenseServiceComponent.expenseService();
        ResponseBody responseBody = gson.fromJson(input.getBody(), ResponseBody.class);

        try {
            //Expense expense = gson.fromJson(input.getBody(), Expense.class);
            String id = expenseService.createExpenseList(responseBody.getEmail(), responseBody.getTitle());

            return response
                    .withStatusCode(200)
                    .withBody(id);
        } catch (InvalidDataException e) {
            return response
                    .withStatusCode(418)
                    .withBody(gson.toJson(e.errorPayload()));
        } catch (Exception e) {
            return response
                    .withStatusCode(418);
        }
    }
}
