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
# Creates a client for the given buyer account ID.

require 'optparse'
require 'securerandom'

require_relative '../../../util'


def create_clients(marketplace, options)
  parent = "buyers/#{options[:account_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::Client.new(
    display_name: options[:display_name],
    role: options[:role],
    seller_visible: options[:seller_visible]
  )

  partner_client_id = options[:partner_client_id]
  unless partner_client_id.nil?
    body.partner_client_id = partner_client_id
  end

  puts "Creating client for buyer account '#{parent}'"

  client = marketplace.create_buyer_client(parent, body)
  ClientPrinter.print(client)
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
      'The resource ID of the buyers resource under which the client is to be created.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'display_name',
      'The display name shown to publishers. Must be unique for clients without parnerClientId specified. '\
      'The maximum length allowed is 255 characters. By default, this sample will specify a generated name.',
      short_alias: 'n', required: false, default_value: "Test Client #{SecureRandom.uuid}"
    ),
    Option.new(
      'partner_client_id',
      'Arbitrary unique identifier provided by the buyer. This field can be used to associate a client with '\
      'an identifier in the namespace of the buyer. If present, it must be unique across all the clients. By '\
      'default, this sample will not specify a partnerClientId.',
      short_alias: 'p', required: false, default_value: nil
    ),
    Option.new(
      'role',
      'The role assigned to the client, which determines its permissions. By default, this will be set to '\
      'CLIENT_DEAL_VIEWER. For more details on how to interpret the different roles, see:'\
      'https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest/v1/buyers.clients#ClientRole',
      short_alias: 'r', required: false, default_value: 'CLIENT_DEAL_VIEWER'
    ),
    Option.new(
      'seller_visible',
      'Whether the client will be visible to publishers. By default, this sample will set this to false.',
      short_alias: 's', required: false, default_value: false
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    create_clients(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
