/* Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

using Google.Apis.AuthorizedBuyersMarketplace.v1;
using Google.Apis.AuthorizedBuyersMarketplace.v1.Data;
using Mono.Options;

using System;
using System.Collections.Generic;

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.Proposals
{
    /// <summary>
    /// Adds a note to a given proposal.
    /// </summary>
    public class AddNoteToProposals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public AddNoteToProposals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example adds a note to a given proposal.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {
                "account_id", "proposal_id"};
            bool showHelp = false;

            string accountId = null;
            string proposalId = null;
            string note = null;

            OptionSet options = new OptionSet {
                "Adds a note to a given proposal.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "proposal is being accessed. This will be used to construct the " +
                     "proposal name used as a path parameter for the proposals.addNote request."),
                    a => accountId = a
                },
                {
                    "p|proposal_id=",
                    ("[Required] The resource ID of the buyers.proposals resource that a note " +
                     "is being added to. This will be used to construct the proposal name " +
                     "used as a path parameter for the proposals.addNote request."),
                    p => proposalId = p
                },
                {
                    "n|note=",
                    ("The note to be added to the proposal. "),
                    n => note = n
                },
            };

            List<string> extras = options.Parse(exampleArgs);
            var parsedArgs = new Dictionary<string, object>();

            // Show help message.
            if (showHelp == true)
            {
                options.WriteOptionDescriptions(Console.Out);
                Environment.Exit(0);
            }
            // Set optional arguments.
            parsedArgs["account_id"] = accountId;
            parsedArgs["proposal_id"] = proposalId;
            parsedArgs["note"] = note ?? "Created note from C# sample.";
            // Validate that options were set correctly.
            Utilities.ValidateOptions(options, parsedArgs, requiredOptions, extras);

            return parsedArgs;
        }

        /// <summary>
        /// Run the example.
        /// </summary>
        /// <param name="parsedArgs">Parsed arguments for the example.</param>
        protected override void Run(Dictionary<string, object> parsedArgs)
        {
            string accountId = (string) parsedArgs["account_id"];
            string proposalId = (string) parsedArgs["proposal_id"];
            string noteValue = (string) parsedArgs["note"];
            string proposalName = $"buyers/{accountId}/proposals/{proposalId}";

            Note note = new Note();
            note.NoteValue = noteValue;

            AddNoteRequest addNoteRequest = new AddNoteRequest()
            {
                Note = note
            };

            Console.WriteLine("Adding note to proposal with name \"{0}\":", proposalName);

            BuyersResource.ProposalsResource.AddNoteRequest request =
                mkService.Buyers.Proposals.AddNote(addNoteRequest, proposalName);
            Proposal response = null;

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Marketplace API returned error response:\n{exception.Message}");
            }

            Utilities.PrintProposal(response);
        }
    }
}
