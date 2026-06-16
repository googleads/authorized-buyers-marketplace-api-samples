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
# Creates a client user for the given buyer account ID and client ID.
#
# When a client user is created, the specified email address will receive an
# email to confirm access to the Authorized Buyers UI. It will remain in the
# "INVITED" state and be unable to access the UI until the specified email has
# approved of the change.

require 'optparse'

require_relative '../../../../util'


def create_client_users(marketplace, options)
  parent = "buyers/#{options[:account_id]}/clients/#{options[:client_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::ClientUser.new(
    email: options[:email]
  )

  puts "Creating client user for client with name '#{parent}'"

  client_user = marketplace.create_buyer_client_user(parent, body)
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
      'The resource ID of the buyers resource under which the client user is to be created. This will be used to '\
      'construct the name used as a path parameter for the users.create request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'client_id',
      'The resource ID of the buyers.clients resource under which the client user is to be created. This will be '\
      'used to construct the name used as a path parameter for the users.create request.',
      short_alias: 'c', required: true, default_value: nil
    ),
    Option.new(
      'email',
      'The client user\'s email address that has to be unique across all client users for a given client. By '\
      'default, this will be set to a randomly generated email for demonstration purposes.',
      short_alias: 'e', required: false, default_value: "testemail#{rand(10000000...99999999)}@test.com"
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    create_client_users(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
