#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2022 Google LLC
#
# License:: Licensed under the Apache License, Version 2.0 (the "License");
#           you may not use this file except in compliance with the License.
#           You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#           Unless required by applicable law or agreed to in writing, software
#           distributed under the License is distributed on an "AS IS" BASIS,
#           WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#           implied.
#           See the License for the specific language governing permissions and
#           limitations under the License.
#
# Sample application that authenticates and makes an API request.

require 'google/apis/authorizedbuyersmarketplace_v1'
require 'googleauth/service_account'

# You can download the JSON keyfile used for authentication from the Google
# Developers Console.
KEY_FILE = 'path_to_key'  # Path to JSON file containing your private key.

# Name of the buyer resource for which the API call is being made.
BUYER_NAME = 'insert_buyer_resource_name'


def first_api_request()
  # Create credentials using the JSON key file.
  auth_options = {
    :json_key_io => File.open(KEY_FILE, "r"),
    :scope => 'https://www.googleapis.com/auth/authorized-buyers-marketplace'
  }

  oauth_credentials = Google::Auth::ServiceAccountCredentials.make_creds(
    options=auth_options
  )

  # Create the service and set credentials
  marketplace = (
    Google::Apis::AuthorizedbuyersmarketplaceV1::AuthorizedBuyersMarketplaceService.new
  )
  marketplace.authorization = oauth_credentials
  marketplace.authorization.fetch_access_token!

  begin
    # Call the buyers.clients.list method to get list of clients for given buyer.
    clients_list = marketplace.list_buyer_clients(BUYER_NAME)

    if clients_list.clients.any?
      puts "Found the following clients for buyer '%s':" % BUYER_NAME
      clients_list.clients.each do |client|
        puts "* Client name: #{client.name}"
      end
    else
      puts "No clients were found that were associated with buyer '%s'" % BUYER_NAME
    end
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n%s" % e.message
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n%s" % e.message
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n%s" % e.message
  end
end

if __FILE__ == $0
  begin
    first_api_request()
  end
end
