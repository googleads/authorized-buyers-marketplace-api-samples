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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.Proposals.Deals
{
    /// <summary>
    /// Patches the user list targeting of one or more deals for the given buyer's proposal.
    ///
    /// This operation requires that the deals all exist under the same proposal.
    ///
    /// The user list targeting of the given deals will be modified to target the specified
    /// user lists. User lists can be retrieved via the Real-time Bidding API's
    /// buyers.userLists resource. You can learn more about buyers.userLists in the reference
    /// documentation:
    /// https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/v1/buyers.userLists
    ///
    /// Note: Only preferred and programmatic guaranteed deals can be modified by the buyer;
    /// attempting to modify a private auction deal will result in an error response.
    /// </summary>
    public class BatchUpdateDeals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public BatchUpdateDeals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example patches the user list targeting of one or more deals for " +
                "the given buyer's proposal.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "proposal_id", "deal_ids",
                "proposal_revision", "user_list_ids"};
            bool showHelp = false;

            string accountId = null;
            IList<string> dealIds = new List<string>();
            string proposalId = null;
            long? proposalRevision = null;
            IList<long?> userListIds = new List<long?>();

            OptionSet options = new OptionSet {
                "Patches the user list targeting of one or more deals for the given buyer's " +
                "proposal.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which one or more " +
                     "deals are being patched. This will be used to construct the proposal " +
                     "name used as a path parameter for the deals.batchUpdate request, and each " +
                     "deal name included in the request body."),
                    a => accountId = a
                },
                {
                    "d|deal_id=",
                    ("[Required] The resource ID of one or more buyers.proposals.deals " +
                     "resources that will be patch in a batch update operation. These will be " +
                     "used to construct the deal names included in the request body. Specify " +
                     "this argument for each deal you intend to patch with this example."),
                    d => dealIds.Add(d)
                },
                {
                    "p|proposal_id=",
                    ("[Required] The resource ID of the buyers.proposals resource under which " +
                     "one or more deals are being patched. This will be used to construct the " +
                     "name used as a path parameter for the deals.batchUpdate request, and each " +
                     "deal name included in the request body."),
                    p => proposalId = p
                },
                {
                    "r|proposal_revision=",
                    ("[Required] The revision number for the corresponding proposal of the " +
                     "deals being modified. Each update to the proposal or its deals causes the " +
                     "number to increment. The revision number specified must match the value " +
                     "stored server-side in order for the operation to be performed."),
                    (long? r) => proposalRevision = r
                },
                {
                    "u|user_list_id=",
                    ("[Required] The resource ID of one or more buyers.userLists resources that " +
                     "are to be targeted by the given deals. Specify this argument for each " +
                     "user list you intend to target."),
                    (long? u) => userListIds.Add(u)
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
            // Set arguments.
            parsedArgs["account_id"] = accountId;
            parsedArgs["deal_ids"] = dealIds;
            parsedArgs["proposal_id"] = proposalId;
            parsedArgs["proposal_revision"] = proposalRevision;
            parsedArgs["user_list_ids"] = userListIds.Count > 0 ? userListIds : null;

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
            IList<string> dealIds = (List<string>) parsedArgs["deal_ids"];
            string proposalId = (string) parsedArgs["proposal_id"];
            string parent = $"buyers/{accountId}/proposals/{proposalId}";
            long? proposalRevision = (long?) parsedArgs["proposal_revision"];
            IList<long?> userListIds = (List<long?>) parsedArgs["user_list_ids"];

            IList<UpdateDealRequest> updateDealRequests = new List<UpdateDealRequest>();

            // Populate the request body based on the deals specified.
            foreach (string dealId in dealIds)
            {
                UpdateDealRequest updateDealRequest = new UpdateDealRequest()
                {
                    Deal = new Deal()
                    {
                        Name = $"buyers/{accountId}/proposals/{proposalId}/deals/{dealId}",
                        ProposalRevision = proposalRevision,
                        Targeting = new MarketplaceTargeting()
                        {
                            UserListTargeting = new CriteriaTargeting()
                            {
                                TargetedCriteriaIds = userListIds
                            }
                        }
                    },
                    UpdateMask = "targeting.userListTargeting.targetedCriteriaIds"
                };

                updateDealRequests.Add(updateDealRequest);
            }

            BatchUpdateDealsRequest batchUpdateDealsRequest = new BatchUpdateDealsRequest()
            {
                Requests = updateDealRequests
            };

            BuyersResource.ProposalsResource.DealsResource.BatchUpdateRequest request =
                mkService.Buyers.Proposals.Deals.BatchUpdate(batchUpdateDealsRequest, parent);
            BatchUpdateDealsResponse response = null;

            Console.WriteLine("Batch updating deals for proposal with name: {0}", parent);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            foreach (Deal deal in response.Deals)
            {
                Utilities.PrintDeal(deal);
            }
        }
    }
}
