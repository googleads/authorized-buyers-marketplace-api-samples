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
import com.google.api.services.authorizedbuyersmarketplace.v1.model.AddNoteRequest;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Note;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Proposal;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Adds a note to a given proposal. */
public class AddNoteToProposals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String proposalId = parsedArgs.getString(("proposal_id"));
    String proposalName = String.format("buyers/%d/proposals/%s", accountId, proposalId);
    String noteValue = parsedArgs.getString("note");

    Note note = new Note();
    note.setNote(noteValue);

    AddNoteRequest addNoteRequest = new AddNoteRequest();
    addNoteRequest.setNote(note);

    Proposal proposal = null;
    try {
      proposal =
          marketplaceClient.buyers().proposals().addNote(proposalName, addNoteRequest).execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf("Adding note to proposal with name \"%s\":%n", proposalName);
    Utils.printProposal(proposal);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("AddNoteToProposals")
            .build()
            .defaultHelp(true)
            .description(("Adds a note to a given proposal."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the proposal is being "
                + "accessed. This will be used to construct the proposal name used as a path "
                + "parameter for the proposals.addNote request.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-p", "--proposal_id")
        .help(
            "The resource ID of the proposals resource that a note is being added to. This "
                + "will be used to construct the name used as a path parameter for the "
                + "proposals.addNote request.")
        .required(true)
        .type(String.class);
    parser
        .addArgument("-n", "--note")
        .help("The note to be added to the proposal.")
        .type(String.class)
        .setDefault("Created note from Java sample.");

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
