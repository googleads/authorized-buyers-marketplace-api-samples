/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.proposals.deals;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.BatchUpdateDealsRequest;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.BatchUpdateDealsResponse;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.CriteriaTargeting;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Deal;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.MarketplaceTargeting;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.UpdateDealRequest;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Patches the user list targeting of one or more deals for the given buyer's proposal.
 *
 * <p>This operation requires that the deals all exist under the same proposal.
 *
 * <p>The user list targeting of the given deals will be modified to target the specified user
 * lists. User lists can be retrieved via the Real-time Bidding API's buyers.userLists resource. You
 * can learn more about buyers.userLists in the reference documentation:
 * https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/v1/buyers.userLists
 *
 * <p>Note: Only preferred and programmatic guaranteed deals can be modified by the buyer;
 * attempting to modify a private auction deal will result in an error response.
 */
public class BatchUpdateDeals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    List<Long> dealIds = parsedArgs.getList("deal_ids");
    String proposalId = parsedArgs.getString("proposal_id");
    String parent = String.format("buyers/%d/proposals/%s", accountId, proposalId);
    Long proposalRevision = parsedArgs.getLong("proposal_revision");
    List<Long> userListIds = parsedArgs.getList("user_list_ids");

    List<UpdateDealRequest> updateDealRequests = new ArrayList<>();

    // Populate the request body based on the deals specified.
    for (Long dealId : dealIds) {
      Deal deal = new Deal();
      deal.setName(String.format("buyers/%d/proposals/%s/deals/%d", accountId, proposalId, dealId));
      deal.setProposalRevision(proposalRevision);

      CriteriaTargeting userListTargeting = new CriteriaTargeting();
      userListTargeting.setTargetedCriteriaIds(userListIds);

      MarketplaceTargeting marketplaceTargeting = new MarketplaceTargeting();
      marketplaceTargeting.setUserListTargeting(userListTargeting);
      deal.setTargeting(marketplaceTargeting);

      UpdateDealRequest updateDealRequest = new UpdateDealRequest();
      updateDealRequest.setDeal(deal);
      updateDealRequest.setUpdateMask("targeting.userListTargeting.targetedCriteriaIds");

      updateDealRequests.add(updateDealRequest);
    }

    BatchUpdateDealsRequest batchUpdateDealsRequest = new BatchUpdateDealsRequest();
    batchUpdateDealsRequest.setRequests(updateDealRequests);

    BatchUpdateDealsResponse response = null;
    try {
      response =
          marketplaceClient
              .buyers()
              .proposals()
              .deals()
              .batchUpdate(parent, batchUpdateDealsRequest)
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf("Batch updating deals for proposal with name \"%s\":%n", parent);

    for (Deal deal : response.getDeals()) {
      Utils.printDeal(deal);
    }
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("BatchUpdateDeals")
            .build()
            .defaultHelp(true)
            .description(
                ("Patches the user list targeting of one or more deals for the given "
                    + "buyer's proposal."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which one or more deals are being"
                + " patched. This will be used to construct the proposal name used as a path"
                + " parameter for the deals.batchUpdate request, and each deal name included in the"
                + " request body.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-d", "--deal_ids")
        .help(
            "The resource ID of one or more buyers.proposals.deals resources that will be patched"
                + " in a batch update operation. These will be used to construct the deal names"
                + " included in the request body. Specify each client ID separated by a space.")
        .required(true)
        .type(Long.class)
        .nargs("+");
    parser
        .addArgument("-p", "--proposal_id")
        .help(
            "The resource ID of the buyers.proposals resource under which one or more deals is"
                + " being patched. This will be used to construct the name used as a path parameter"
                + " for the deals.batchUpdate request, and each deal name included in the request"
                + " body")
        .required(true);
    parser
        .addArgument("-r", "--proposal_revision")
        .help(
            "The revision number for the corresponding proposal of the deals being modified. Each"
                + " update to the proposal or its deals causes the number to increment. The"
                + " revision number specified must match the value stored server-side in order for"
                + " the operation to be performed.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-u", "--user_list_ids")
        .help(
            "The resource ID of one or more buyers.userLists resources that are to be targeted "
                + "by the given deals. Specify each client ID separated by a space.")
        .required(true)
        .type(Long.class)
        .nargs("+");

    Namespace parsedArgs = null;
    try {
      parsedArgs = parser.parseArgs(args);
    } catch (ArgumentParserException ex) {
      parser.handleError(ex);
      System.exit(1);
    }

    AuthorizedBuyersMarketplace client = null;
    try {
      client = Utils.getMarketplaceClient();
    } catch (IOException ex) {
      System.out.printf("Unable to create Marketplace API service:%n%s", ex);
      System.out.println("Did you specify a valid path to a service account key file?");
      System.exit(1);
    } catch (GeneralSecurityException ex) {
      System.out.printf("Unable to establish secure HttpTransport:%n%s", ex);
      System.exit(1);
    }

    execute(client, parsedArgs);
  }
}
