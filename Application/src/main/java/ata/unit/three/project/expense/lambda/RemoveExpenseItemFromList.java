package ata.unit.three.project.expense.lambda;

import ata.unit.three.project.App;
import ata.unit.three.project.expense.service.DaggerExpenseServiceComponent;
import ata.unit.three.project.expense.service.ExpenseService;
import ata.unit.three.project.expense.service.ExpenseServiceComponent;
import ata.unit.three.project.expense.service.exceptions.InvalidDataException;
import ata.unit.three.project.expense.service.exceptions.ItemNotFoundException;
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
public class RemoveExpenseItemFromList
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

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


        // Your Code Here
        //    It shouldn't be possible to add/remove an expense item where The email of the expense item does not match the expense list
        //    It should not be possible to remove an expense item that does not exist in the list

        try {
            expenseService.removeExpenseItemFromList((responseBody.getExpenseListId()), responseBody.getExpenseItemId());

            return response
                    .withStatusCode(200);

        } catch (InvalidDataException e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.errorPayload()));
        } catch (ItemNotFoundException e) {
            return response
                    .withStatusCode(400);
        }
    }
}
