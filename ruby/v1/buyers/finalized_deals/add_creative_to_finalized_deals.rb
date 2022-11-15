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
# Adds a creative to a given finalized deal that will be used in bids.
#
# It is recommended that those configuring programmatic guaranteed deals use
# this method to associate at least one creative that is ready to be placed in
# bids with the deal before signaling that the deal is ready to begin serving
# with finalizedDeals.setReadyToServe.
#
# A buyer's creatives can be viewed with the Real-time Bidding API:
# https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/v1/buyers.creatives

require 'optparse'

require_relative '../../../util'


def add_creative_to_finalized_deals(marketplace, options)
  buyer = "buyers/#{options[:account_id]}"
  name = "#{buyer}/finalizedDeals/#{options[:deal_id]}"
  creative = "#{buyer}/creatives/#{options[:creative_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::AddCreativeRequest.new(
    creative: creative,
  )

  puts "Adding creative with name '#{creative}' to finalized deal with name '#{name}':"

  finalized_deal = marketplace.add_finalized_deal_creative(name, body)
  FinalizedDealPrinter.print(finalized_deal)
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
      'The resource ID of the buyers resource under which the finalized deal was created. This will be used to '\
      'construct the name used as a path parameter for the finalizedDeals.addCreative request, and the creative name '\
      'included in the request body.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'creative_id',
      'The resource ID of the buyers.creatives resource that is to be added to the finalized deal. This will be used '\
      'to construct the creative name included in the finalizedDeals.addCreative request body.',
      short_alias: 'c', required: true, default_value: nil
    ),
    Option.new(
      'deal_id',
      'The resource ID of the buyers.finalizedDeals resource that the creative is being added to. This will be used '\
      'to construct the name used as a path parameter for the finalizedDeals.addCreative request.',
      short_alias: 'd', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    add_creative_to_finalized_deals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
