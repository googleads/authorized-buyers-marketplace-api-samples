<?php

/**
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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_Clients_Users;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\DeactivateClientUserRequest;
use Google\Service\Exception as GoogleServiceException;

/**
 * This example shows how to deactivate a client user with the given account, client, and user IDs.
 */
class DeactivateClientUsers extends BaseExample
{
    public function __construct($client)
    {
        $this->service = Config::getGoogleServiceAuthorizedBuyersMarketplace($client);
    }

    /**
     * @see BaseExample::getInputParameters()
     */
    protected function getInputParameters()
    {
        return [
            [
                'name' => 'account_id',
                'display' => 'Account ID',
                'description' =>
                    'The resource ID of the buyers resource under which the client user was ' .
                    'created. This will be used to construct the name used as a path parameter ' .
                    'for the users.deactivate request.',
                'required' => true
            ],
            [
                'name' => 'client_id',
                'display' => 'Client ID',
                'description' =>
                    'The resource ID of the buyers.clients resource under which the client user ' .
                    'was created. This will be used to construct the name used as a path ' .
                    'parameter for the users.deactivate request.',
                'required' => true
            ],
             [
                'name' => 'client_user_id',
                'display' => 'Client User ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers.clients.users resource that is to be ' .
                    'deactivated. This will be used to construct the name used as a path ' .
                    'parameter for the users.deactivate request.'
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $name = "buyers/$values[account_id]/clients/$values[client_id]/users/" .
                "$values[client_user_id]";

        $deactivateRequest = new DeactivateClientUserRequest();

        try {
            $result = $this->service->buyers_clients_users->deactivate($name, $deactivateRequest);
            print "<h2>Deactivating client user '$name' was successful!</h2>";
            $this->printResult($result);
        } catch (GoogleServiceException $ex) {
            print "<h2>Deactivating client user '$name' was unsuccessful.</h2>";
            $errorResponse = $ex->getMessage();
            print "<p>Error response:</br><code>$errorResponse</code></p>";
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Deactivate Client Users';
    }
}
