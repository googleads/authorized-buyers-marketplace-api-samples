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
    /// Sends a request for proposal to a publisher for a preferred deal.
    ///
    /// The publisher will be sent an RFP that will initiate negotiation for a preferred deal. For
    /// the buyer, this will create a corresponding proposal.
    ///
    /// You must refer to the publisher using their publisher profile. These can be found with the
    /// buyers.publisherProfiles resource.
    /// </summary>
    public class SendRfpForPreferredDealProposals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public SendRfpForPreferredDealProposals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example sends a request for proposal for a preferred deal to a " +
                "publisher for a given buyer account ID.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "buyer_contacts_display_name",
                "buyer_contacts_email", "publisher_profile_id"};
            bool showHelp = false;

            string accountId = null;
            string buyerContactsDisplayName = null;
            string buyerContactsEmail = null;
            string displayName = null;
            string publisherProfileId = null;

            OptionSet options = new OptionSet {
                "Sends a request for proposal to a publisher for the given buyer account ID.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource for which the RFP is " +
                     "being sent to the publisher. This will be used to construct the name used " +
                     "as a path parameter for the proposals.sendRfp request."),
                    a => accountId = a
                },
                {
                    "d|buyer_contacts_display_name=",
                    ("[Required] The display name of the buyer's contact, which will be visible " +
                     "to the publisher."),
                    d => buyerContactsDisplayName = d
                },
                {
                    "e|buyer_contacts_email=",
                    ("[Required] Email address for the buyer's contact, which will be visible " +
                     "to the publisher."),
                    e => buyerContactsEmail = e
                },
                {
                    "p|publisher_profile_id=",
                    ("[Required] The resource ID of the publisher profiles resource " +
                     "representing the publisher that the buyer wants to send the RFP."),
                    p => publisherProfileId = p
                },
                {
                    "n|display_name=",
                    ("The display name of the proposal being created by the RFP."),
                    n => displayName = n
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
            parsedArgs["buyer_contacts_display_name"] = buyerContactsDisplayName;
            parsedArgs["buyer_contacts_email"] = buyerContactsEmail;
            parsedArgs["publisher_profile_id"] = publisherProfileId;
            parsedArgs["display_name"] = displayName ?? String.Format(
                "Test PD Proposal {0}",
                System.Guid.NewGuid());

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
            string publisherProfileId = (string) parsedArgs["publisher_profile_id"];
            string parent = $"buyers/{accountId}";
            string publisherProfileName =
                $"buyers/{accountId}/publisherProfiles/{publisherProfileId}";
            DateTime startTime = DateTime.Now.AddDays(1);

            SendRfpRequest rfp = new SendRfpRequest()
            {
                DisplayName = (string) parsedArgs["display_name"],
                PublisherProfile = publisherProfileName,
                Note = "Test preferred deal proposal created by C# sample.",
                // Specify start and end flight times in RFC3339 UTC "Zulu" format.
                FlightStartTime = startTime.ToUniversalTime().ToString("o"),
                FlightEndTime = startTime.AddDays(1).ToUniversalTime().ToString("o"),
                BuyerContacts = new List<Contact>()
                {
                     new Contact()
                     {
                         Email = (string) parsedArgs["buyer_contacts_email"],
                         DisplayName = (string) parsedArgs["buyer_contacts_display_name"]
                     }
                },
                GeoTargeting = new CriteriaTargeting()
                {
                    TargetedCriteriaIds = new List<long?>()
                    {
                        // Target New York, NY
                        1023191L
                    }
                },
                InventorySizeTargeting = new InventorySizeTargeting()
                {
                    TargetedInventorySizes = new List<AdSize>()
                    {
                        new AdSize()
                        {
                            Width = 300L,
                            Height = 260L,
                            Type = "PIXEL"
                        }
                    }
                },
                PreferredDealTerms = new PreferredDealTerms()
                {
                    FixedPrice = new Price()
                    {
                        Type = "CPM",
                        Amount = new Money()
                        {
                            CurrencyCode = "USD",
                            Units = 1,
                            Nanos = 1
                        }
                    }
                }
            };

            BuyersResource.ProposalsResource.SendRfpRequest request =
                mkService.Buyers.Proposals.SendRfp(rfp, parent);
            Proposal response = null;

            Console.WriteLine("Sending preferred deal RFP for buyer: {0}", parent);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            Utilities.PrintProposal(response);
        }
    }
}
