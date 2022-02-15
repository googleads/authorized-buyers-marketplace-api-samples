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
import com.google.api.services.authorizedbuyersmarketplace.v1.model.CancelNegotiationRequest;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Proposal;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Cancels the ongoing negotiation for the specified proposal.
 *
 * <p>This method is not supported for proposals including private auction deals because negotiation
 * for that deal type can not be canceled. On successful cancelation, the proposal's state will be
 * set to TERMINATED.
 *
 * <p>This does not cancel or end serving for deals that have already been finalized. For finalized
 * deals that are under renegotiation, calling this method will instead reset the proposal's state
 * to FINALIZED.
 */
public class CancelNegotiationForProposals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String proposalId = parsedArgs.getString("proposal_id");
    String name = String.format("buyers/%d/proposals/%s", accountId, proposalId);

    CancelNegotiationRequest cancelNegotiationRequest = new CancelNegotiationRequest();

    Proposal proposal = null;

    try {
      proposal =
          marketplaceClient
              .buyers()
              .proposals()
              .cancelNegotiation(name, cancelNegotiationRequest)
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf("Canceling negotiation for a proposal with name \"%s\":%n", name);
    Utils.printProposal(proposal);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("CancelNegotiationForProposals")
            .build()
            .defaultHelp(true)
            .description(
                ("Cancels negotiation for a proposal with the given account and proposal "
                    + "IDs."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the proposal exists. This will "
                + "be used to construct the name used as a path parameter for the "
                + "proposals.cancelNegotiaton request.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-p", "--proposal_id")
        .help(
            "The resource ID of the buyers.proposals resource that is being canceled. This "
                + "will be used to construct the name used as a path parameter for the "
                + "proposals.cancelNegotiation request.")
        .required(true);

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
