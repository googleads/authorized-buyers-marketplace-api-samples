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
import com.google.api.services.authorizedbuyersmarketplace.v1.model.PrivateData;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Proposal;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Patches a specified proposal at the given revision number.
 *
 * <p>Fields that can be patched for this resource can be found in the reference documentation:
 * https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest/v1/buyers.proposals
 *
 * <p>Note: If the revision number is lower than what is stored for the proposal server-side, the
 * operation will be deemed obsolete and an error will be returned.
 *
 * <p>Only proposals for preferred and programmatic guaranteed deals can be modified by buyers.
 */
public class PatchProposals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    String proposalId = parsedArgs.getString(("proposal_id"));
    String name = String.format("buyers/%d/proposals/%s", accountId, proposalId);
    Long proposalRevision = parsedArgs.getLong("proposal_revision");

    PrivateData buyerPrivateData = new PrivateData();
    buyerPrivateData.setReferenceId(
        String.format("Marketplace-Java-Sample-Reference-%s", UUID.randomUUID()));

    Proposal patchedProposal = new Proposal();
    patchedProposal.setProposalRevision(proposalRevision);
    patchedProposal.setBuyerPrivateData(buyerPrivateData);

    String updateMask = "buyerPrivateData.referenceId";

    Proposal proposal = null;
    try {
      proposal =
          marketplaceClient
              .buyers()
              .proposals()
              .patch(name, patchedProposal)
              .setUpdateMask(updateMask)
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf("Patching proposal with name \"%s\":%n", name);
    Utils.printProposal(proposal);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("PatchProposals")
            .build()
            .defaultHelp(true)
            .description(("Patches a proposal at the given revision number."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource for which the RFP is being patched. This "
                + "will be used to construct the name used as a path parameter for the "
                + "proposals.patch request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-p", "--proposal_id")
        .help(
            "The resource ID of the proposals resource that is being patched. This will be used to"
                + " construct the name used as a path parameter for the proposals.patch request.")
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
