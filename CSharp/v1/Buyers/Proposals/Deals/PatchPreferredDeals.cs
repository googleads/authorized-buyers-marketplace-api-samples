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
    /// Patches a preferred deal at the given revision number.
    ///
    /// This will modify the deal's flightStartTime, flightEndTime, and preferredDealTerms.
    ///
    /// Note: If the revision number is lower than what is stored for the deal server-side, the
    /// operation will be deemed obsolete and an error will be returned.
    /// </summary>
    public class PatchPreferredDeals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public PatchPreferredDeals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example patches a preferred deal at the given revision number.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "proposal_id", "deal_id",
                "proposal_revision"};
            bool showHelp = false;

            string accountId = null;
            string dealId = null;
            string proposalId = null;
            long? proposalRevision = null;
            long? fixedPriceUnits = null;
            int? fixedPriceNanos = null;
            OptionSet options = new OptionSet {
                "Patches a preferred deal at the given revision number.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the deal is " +
                     "being patched. This will be used to construct the name used as a path " +
                     "parameter for the deals.patch request."),
                    a => accountId = a
                },
                {
                    "d|deal_id=",
                    ("[Required] The resource ID of the buyers.proposals.deals resource that is " +
                     "being patched. This will be used to construct the name used as a path " +
                     "parameter for the deals.patch request."),
                    d => dealId = d
                },
                {
                    "p|proposal_id=",
                    ("[Required] The resource ID of the buyers.proposals resource under which " +
                     "the deal is being patched. This will be used to construct the name used " +
                     "as a path parameter for the deals.patch request."),
                    p => proposalId = p
                },
                {
                    "r|proposal_revision=",
                    ("[Required] The revision number for the corresponding proposal of the deal " +
                     "being modified. Each update to the proposal or its deals causes the " +
                     "number to increment. The revision number specified must match the value " +
                     "stored server-side in order for the operation to be performed."),
                    (long? r) => proposalRevision = r
                },
                {
                    "u|fixed_price_units=",
                    "Whole units of the currency specified for the preferred deal.",
                    (long? u) => fixedPriceUnits = u
                },
                {
                    "n|fixed_price_nanos=",
                    "Number of nano units of the currency specified for the preferred deal.",
                    (int? u) => fixedPriceNanos = u
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
            parsedArgs["deal_id"] = dealId;
            parsedArgs["proposal_id"] = proposalId;
            parsedArgs["proposal_revision"] = proposalRevision;
            parsedArgs["fixed_price_units"] = fixedPriceUnits ?? 1L;
            parsedArgs["fixed_price_nanos"] = fixedPriceNanos ?? 500000000;

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
            string dealId = (string) parsedArgs["deal_id"];
            string proposalId = (string) parsedArgs["proposal_id"];
            string name = $"buyers/{accountId}/proposals/{proposalId}/deals/{dealId}";
            long? proposalRevision = (long?) parsedArgs["proposal_revision"];
            DateTime startTime = DateTime.Now.AddDays(1);

            Deal preferredDeal = new Deal()
            {
                ProposalRevision = proposalRevision,
                // Patch new start and end flight times in RFC3339 UTC "Zulu" format.
                FlightStartTime = startTime.ToUniversalTime().ToString("o"),
                FlightEndTime = startTime.AddDays(1).ToUniversalTime().ToString("o"),
                PreferredDealTerms = new PreferredDealTerms()
                {
                    FixedPrice = new Price()
                    {
                        Amount = new Money()
                        {
                            Units = (long?) parsedArgs["fixed_price_units"],
                            Nanos = (int?) parsedArgs["fixed_price_nanos"]
                        }
                    }
                }
            };

            string updateMask = "flightStartTime,flightEndTime," +
                "preferredDealTerms.fixedPrice.amount.units," +
                "preferredDealTerms.fixedPrice.amount.nanos";

            BuyersResource.ProposalsResource.DealsResource.PatchRequest request =
                mkService.Buyers.Proposals.Deals.Patch(preferredDeal, name);
            request.UpdateMask = updateMask;
            Deal response = null;

            Console.WriteLine("Patching deal with name: {0}", name);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            Utilities.PrintDeal(response);
        }
    }
}
