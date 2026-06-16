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
    /// Patches a client with the specified name.
    /// </summary>
    public class PatchClients : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public PatchClients()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example patches a client having the specified name.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "client_id"};
            bool showHelp = false;

            string accountId = null;
            string clientId = null;
            string displayName = null;

            OptionSet options = new OptionSet {
                "Patches the specified client.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "client was created. This will be used to construct the name used as a " +
                     "path parameter for the clients.patch request."),
                    a => accountId = a
                },
                {
                    "c|client_id=",
                    ("[Required] The resource ID of the buyers.clients resource for which the " +
                     "client was created. This will be used to construct the name used as a " +
                     "path parameter for the clients.patch request."),
                    c => clientId = c
                },
                {
                    "d|display_name=",
                    ("The display name shown to publishers. Must be unique for clients without " +
                     "partnerClientId specified. The maximum length allowed is 255 characters. " +
                     "By default, this sample will specify a generated name that will be used " +
                     "to patch the client's existing display name."),
                    d => displayName = d
                }
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
            parsedArgs["client_id"] = clientId;
            parsedArgs["display_name"] = displayName ?? $"Test-Client-{System.Guid.NewGuid()}";

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
            var accountId = (string) parsedArgs["account_id"];
            var clientId = (string) parsedArgs["client_id"];
            var name = $"buyers/{accountId}/clients/{clientId}";

            Client clientPatch = new Client()
            {
                DisplayName = (string) parsedArgs["display_name"]
            };

            BuyersResource.ClientsResource.PatchRequest request =
                mkService.Buyers.Clients.Patch(clientPatch, name);
            // Configure the update mask such that only the displayName is updated. If not set, the
            // patch method would overwrite all other writable fields with a null value.
            request.UpdateMask = "displayName";

            Client response = null;

            Console.WriteLine("Patching client with name: {0}", name);

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
