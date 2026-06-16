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

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.proposals;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.AdSize;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Contact;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.CriteriaTargeting;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.InventorySizeTargeting;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Money;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Price;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.ProgrammaticGuaranteedTerms;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Proposal;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.SendRfpRequest;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Sends a request for proposal to a publisher for a programmatic guaranteed deal.
 *
 * <p>The publisher will be sent an RFP that will initiate negotiation for a programmatic guaranteed
 * deal. For the buyer, this will create a corresponding proposal.
 *
 * <p>You must refer to the publisher using their publisher profile. These can be found with the
 * buyers.publisherProfiles resource.
 */
public class SendRfpForProgrammaticGuaranteedDealProposals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    String publisherProfileId = parsedArgs.getString(("publisher_profile_id"));
    String parent = String.format("buyers/%d", accountId);
    String publisherProfileName =
        String.format("buyers/%d/publisherProfiles/%s", accountId, publisherProfileId);

    SendRfpRequest rfp = new SendRfpRequest();
    rfp.setDisplayName(parsedArgs.getString("display_name"));
    rfp.setPublisherProfile(publisherProfileName);
    rfp.setNote("Test preferred deal proposal created by Java sample.");
    // Specify the start and end flight times in RFC3339 UTC "Zulu" format.
    DateTime startTime = DateTime.now().plusDays(1);
    DateTime endTime = startTime.plusDays(1);
    rfp.setFlightStartTime(startTime.toString(ISODateTimeFormat.dateTime()));
    rfp.setFlightEndTime(endTime.toString(ISODateTimeFormat.dateTime()));

    Contact buyerContact = new Contact();
    buyerContact.setEmail(parsedArgs.getString("buyer_contacts_email"));
    buyerContact.setDisplayName((parsedArgs.getString("buyer_contacts_display_name")));
    List<Contact> buyerContacts = new ArrayList<>();
    buyerContacts.add(buyerContact);
    rfp.setBuyerContacts(buyerContacts);

    CriteriaTargeting geoTargeting = new CriteriaTargeting();
    List<Long> targetedCriteriaIds = new ArrayList<>();
    // Target New York, NY
    targetedCriteriaIds.add(1023191L);
    geoTargeting.setTargetedCriteriaIds(targetedCriteriaIds);
    rfp.setGeoTargeting(geoTargeting);

    AdSize adSize = new AdSize();
    adSize.setWidth(300L);
    adSize.setHeight(260L);
    adSize.setType("PIXEL");
    List<AdSize> targetedInventorySizes = new ArrayList<>();
    targetedInventorySizes.add(adSize);
    InventorySizeTargeting inventorySizeTargeting = new InventorySizeTargeting();
    inventorySizeTargeting.setTargetedInventorySizes(targetedInventorySizes);
    rfp.setInventorySizeTargeting(inventorySizeTargeting);

    Money fixedPriceAmount = new Money();
    fixedPriceAmount.setCurrencyCode("USD");
    fixedPriceAmount.setUnits(1L);
    fixedPriceAmount.setNanos(0);

    Price fixedPrice = new Price();
    fixedPrice.setType("CPM");
    fixedPrice.setAmount(fixedPriceAmount);

    ProgrammaticGuaranteedTerms programmaticGuaranteedTerms = new ProgrammaticGuaranteedTerms();
    programmaticGuaranteedTerms.setGuaranteedLooks(0L);
    programmaticGuaranteedTerms.setMinimumDailyLooks(0L);
    programmaticGuaranteedTerms.setReservationType("STANDARD");
    programmaticGuaranteedTerms.setImpressionCap(0L);
    programmaticGuaranteedTerms.setPercentShareOfVoice(0L);
    programmaticGuaranteedTerms.setFixedPrice(fixedPrice);
    rfp.setProgrammaticGuaranteedTerms(programmaticGuaranteedTerms);

    Proposal proposal = null;
    try {
      proposal = marketplaceClient.buyers().proposals().sendRfp(parent, rfp).execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf(
        "Sending programmatic guaranteed deal RFP for buyer Account ID '%d':%n", accountId);
    Utils.printProposal(proposal);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("SendRfpForProgrammaticGuaranteedDealProposals")
            .build()
            .defaultHelp(true)
            .description(
                ("Sends a request for proposal for a programmatic guaranteed deal to a "
                    + "publisher for the given buyer account ID."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource for which the RFP is being sent to the"
                + " publisher. This will be used to construct the name used as a path parameter for"
                + " the proposals.sendRfp request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-d", "--buyer_contacts_display_name")
        .help("The display name of the buyer's contact, which will be visible to the publisher.")
        .required(true)
        .type(String.class);
    parser
        .addArgument("-e", "--buyer_contacts_email")
        .help("Email address for the buyer's contact, which will be visible to the publisher.")
        .required(true)
        .type(String.class);
    parser
        .addArgument("-p", "--publisher_profile_id")
        .help(
            "The resource ID of the publisher profiles resource representing the publisher "
                + "that the buyer wants to send the RFP.")
        .required(true)
        .type(String.class);
    parser
        .addArgument("-n", "--display_name")
        .help("The display name of the proposal being created by the RFP.")
        .type(String.class)
        .setDefault(String.format("Test PG Proposal %s", UUID.randomUUID()));

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
