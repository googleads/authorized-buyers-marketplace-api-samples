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
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Deal;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Money;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Price;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.ProgrammaticGuaranteedTerms;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Patches a programmatic guaranteed deal at the given revision number.
 *
 * <p>This will modify the deal's flightStartTime, flightEndTime, and programmaticGuaranteedTerms.
 *
 * <p>Note: If the revision number is lower than what is stored for the deal server-side, the
 * operation will be deemed obsolete and an error will be returned.
 */
public class PatchProgrammaticGuaranteedDeals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String proposalId = parsedArgs.getString("proposal_id");
    String dealId = parsedArgs.getString("deal_id");
    String name = String.format("buyers/%d/proposals/%s/deals/%s", accountId, proposalId, dealId);
    Long proposalRevision = parsedArgs.getLong("proposal_revision");

    Deal patchedProgrammaticGuaranteedDeal = new Deal();
    patchedProgrammaticGuaranteedDeal.setProposalRevision(proposalRevision);

    // Patch new start and end flight times in RFC3339 UTC "Zulu" format.
    DateTime startTime = DateTime.now().plusDays(1);
    DateTime endTime = startTime.plusDays(1);
    patchedProgrammaticGuaranteedDeal.setFlightStartTime(
        startTime.toString(ISODateTimeFormat.dateTime()));
    patchedProgrammaticGuaranteedDeal.setFlightEndTime(
        endTime.toString(ISODateTimeFormat.dateTime()));

    Money fixedPriceAmount = new Money();
    fixedPriceAmount.setUnits(parsedArgs.getLong("fixed_price_units"));
    fixedPriceAmount.setNanos(parsedArgs.getInt("fixed_price_nanos"));

    Price fixedPrice = new Price();
    fixedPrice.setAmount(fixedPriceAmount);

    ProgrammaticGuaranteedTerms programmaticGuaranteedTerms = new ProgrammaticGuaranteedTerms();
    programmaticGuaranteedTerms.setFixedPrice(fixedPrice);
    patchedProgrammaticGuaranteedDeal.setProgrammaticGuaranteedTerms(programmaticGuaranteedTerms);

    String updateMask =
        "flightStartTime,flightEndTime,"
            + "programmaticGuaranteedTerms.fixedPrice.amount.units,"
            + "programmaticGuaranteedTerms.fixedPrice.amount.nanos";

    Deal deal = null;
    try {
      deal =
          marketplaceClient
              .buyers()
              .proposals()
              .deals()
              .patch(name, patchedProgrammaticGuaranteedDeal)
              .setUpdateMask(updateMask)
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf("Patching deal with name \"%s\":%n", name);
    Utils.printDeal(deal);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("PatchProgrammaticGuaranteedDeals")
            .build()
            .defaultHelp(true)
            .description(("Patches a programmatic guaranteed deal at the given revision number."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the deal is being patched. "
                + "This will be used to construct the name used as a path parameter for the "
                + "deals.patch request.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-d", "--deal_id")
        .help(
            "The resource ID of the buyers.proposals.deals resource that is being patched. "
                + "This will be used to construct the name used as a path parameter for the "
                + "deals.patch request.")
        .required(true)
        .type(String.class);
    parser
        .addArgument("-p", "--proposal_id")
        .help(
            "The resource ID of the buyers.proposals resource under which the deal is being"
                + " patched. This will be used to construct the name used as a path parameter for"
                + " the deals.patch request.")
        .required(true)
        .type(String.class);
    parser
        .addArgument("-r", "--proposal_revision")
        .help(
            "The revision number for the proposal being modified. Each update to the proposal "
                + "or its deals causes the number to increment. The revision number specified must "
                + "match the value stored server-side in order for the operation to be performed.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-u", "--fixed_price_units")
        .help("Whole units of the currency specified for the programmatic guaranteed deal.")
        .type(Long.class)
        .setDefault(1L);
    parser
        .addArgument("-n", "--fixed_price_nanos")
        .help(
            "Number of nano units of the currency specified for the programmatic guaranteed "
                + "deal.")
        .type(Integer.class)
        .setDefault(500000000);

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
