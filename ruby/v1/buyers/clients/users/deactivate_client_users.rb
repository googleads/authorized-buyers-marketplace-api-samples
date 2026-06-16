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
# Dectivates a client user with the given account, client, and user ID.
#
# Deactivating a client user allows one to temporarily remove a given client
# user's ability to access the Authorized Buyers UI on behalf of a client.
# Access can be restored by calling buyers.clients.users.activate.
#
# Note that a client user in the "INVITED" state can not be deactivated.

require 'optparse'
require 'securerandom'

require_relative '../../../../util'


def deactivate_client_users(marketplace, options)
  name = "buyers/#{options[:account_id]}/clients/#{options[:client_id]}/users/#{options[:client_user_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::DeactivateClientUserRequest.new()

  puts "Deactivating client user with name '#{name}':"

  client_user = marketplace.deactivate_client_user(name, body)
  ClientUserPrinter.print(client_user)
end


if __FILE__ == $0
  begin
    # Retrieve the service used to make API requests.
    service = get_service()
  rescue ArgumentError => e
    raise 'Unable to create service, with error message: #{e.message}'
  rescue Signet::AuthorizationError => e
    raise 'Unable to create service, was the KEY_FILE in util.rb set? Error message: #{e.message}'
  end

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'account_id',
      'The resource ID of the buyers resource under which the client user was created. This will be used to '\
      'construct the name used as a path parameter for the users.deactivate request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'client_id',
      'The resource ID of the buyers.clients resource under which the client user was created. This will be used to '\
      'construct the name used as a path parameter for the users.deactivate request.',
      short_alias: 'c', required: true, default_value: nil
    ),
    Option.new(
      'client_user_id',
      'The resource ID of the buyers.clients.users resource that is being deactivated. This will be used to '\
      'construct the name used as a path parameter for the users.deactivate request.',
      short_alias: 'u', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    deactivate_client_users(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
