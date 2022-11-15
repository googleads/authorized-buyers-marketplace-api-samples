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

/**
 * This example gets a single client user for the given account, client, and user IDs.
 */
class GetClientUsers extends BaseExample
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
                'required' => true,
                'description' =>
                    'The resource ID of the buyers resource under which the client was ' .
                    'created. This will be used to construct the name used as a path parameter ' .
                    'for the clients.get request.'
            ],
            [
                'name' => 'client_id',
                'display' => 'Client ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers.clients resource under which the client ' .
                    'was created. This will be used to construct the name used as a path ' .
                    'parameter for the clients.get request.'
            ],
            [
                'name' => 'client_user_id',
                'display' => 'Client User ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers.clients.users resource under which the ' .
                    'client user was created. This will be used to construct the name used as a ' .
                    'path parameter for the users.get request.'
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

        try {
            $client_user = $this->service->buyers_clients_users->get($name);
            print '<h2>Found client user.</h2>';
            $this->printResult($client_user);
        } catch (Google_Service_Exception $ex) {
            if ($ex->getCode() === 404 || $ex->getCode() === 403) {
                print '<h1>Client user not found or can\'t access client user</h1>';
            } else {
                throw $ex;
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Get Client Users';
    }
}
