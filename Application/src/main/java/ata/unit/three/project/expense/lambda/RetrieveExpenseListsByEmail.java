package ata.unit.three.project.expense.lambda;

import ata.unit.three.project.App;
import ata.unit.three.project.expense.dynamodb.ExpenseItem;
import ata.unit.three.project.expense.dynamodb.ExpenseItemList;
import ata.unit.three.project.expense.service.DaggerExpenseServiceComponent;
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
import java.util.*;

import static java.util.Collections.*;

@ExcludeFromJacocoGeneratedReport
public class RetrieveExpenseListsByEmail
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger log = LogManager.getLogger();

    @Inject
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson(input));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String email = input.getQueryStringParameters().get("email");

        ExpenseServiceComponent expenseServiceComponent = DaggerExpenseServiceComponent.create();
        ExpenseService expenseService = expenseServiceComponent.expenseService();


        try {
            List<ExpenseItemList> list = expenseService.getExpenseListByEmail(email);

            for(ExpenseItemList itemList : list) {
//                for (ExpenseItem item : itemList.getExpenseItems()) {
                    Comparator<ExpenseItem> dateSorter = Comparator.comparing(ExpenseItem::getExpenseDate);
                    Collections.sort(itemList.getExpenseItems(), dateSorter.reversed());
//                }
            }

            String output = gson.toJson(list);
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
