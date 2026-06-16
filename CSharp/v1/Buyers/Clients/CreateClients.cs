/* Copyright 2021 Google LLC
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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.Clients
{
    /// <summary>
    /// Creates a client for the given buyer account ID.
    /// </summary>
    public class CreateClients : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public CreateClients()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example creates a client for the given buyer account ID.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id"};
            bool showHelp = false;

            string accountId = null;
            string displayName = null;
            string partnerClientId = null;
            string role = null;
            string sellerVisible = null;

            OptionSet options = new OptionSet {
                "Creates a client for the given buyer account ID.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "client is to be created. This will be used to construct the name used as " +
                     "a path parameter for the clients.create request."),
                    a => accountId = a
                },
                {
                    "d|display_name=",
                    ("The display name shown to publishers. Must be unique for clients without " +
                     "partnerClientId specified. The maximum length allowed is 255 characters. " +
                     "By default, this sample will specify a generated name."),
                    d => displayName = d
                },
                {
                    "p|partner_client_id=",
                    ("Arbitrary unique identifier provided by the buyer. This field can be used " +
                     "to associate a client with an identifier in the namespace of the buyer. If " +
                     "present, it must be unique across all the clients. By default, this sample " +
                     "will not specify a partnerClientId."),
                    p => partnerClientId = p
                },
                {
                    "r|role=",
                    ("The role assigned to the client, which determines its permissions. By " +
                     "default, this will be set to CLIENT_DEAL_VIEWER. For more details on how " +
                     "to interpret the different roles, see:" +
                     "https://developers.google.com/authorized-buyers/apis/marketplace/" +
                     "reference/rest/v1/buyers.clients#ClientRole"),
                    r => role = r
                },
                {
                    "s|seller_visible=",
                    ("A boolean value indicating whether the client will be visible to " +
                     "publishers. By default, this will be set to false."),
                    s => sellerVisible = s
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
            parsedArgs["display_name"] = displayName ?? String.Format(
                "Test_Client_{0}",
                System.Guid.NewGuid());
            parsedArgs["partner_client_id"] = partnerClientId;
            parsedArgs["role"] = role ?? "CLIENT_DEAL_VIEWER";

            if (sellerVisible != null)
            {
                parsedArgs["seller_visible"] = Boolean.Parse(sellerVisible);
            } else
            {
                parsedArgs["seller_visible"] = false;
            }

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
            string parent = $"buyers/{accountId}";

            Client newClient = new Client()
            {
                DisplayName = (string) parsedArgs["display_name"],
                Role = (string) parsedArgs["role"],
                SellerVisible = (Boolean) parsedArgs["seller_visible"]
            };

            string partnerClientId = (string) parsedArgs["partner_client_id"];
            if (partnerClientId != null)
            {
                newClient.PartnerClientId = partnerClientId;
            }

            BuyersResource.ClientsResource.CreateRequest request =
                mkService.Buyers.Clients.Create(newClient, parent);
            Client response = null;

            Console.WriteLine("Creating client for buyer: {0}", parent);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            Utilities.PrintClient(response);
        }
    }
}
