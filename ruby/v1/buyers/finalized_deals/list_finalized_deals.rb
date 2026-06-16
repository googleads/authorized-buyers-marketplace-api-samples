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
# Lists finalized deals for the given buyers and their clients.

require 'optparse'

require_relative '../../../util'


def list_finalized_deals(marketplace, options)
  name = "buyers/#{options[:account_id]}"

  page_token = nil

  puts "Listing finalized deals for buyer with name: \"#{name}\"."
  begin
    response = marketplace.list_buyer_finalized_deals(
        name,
        filter: options[:filter],
        order_by: options[:order_by],
        page_size: options[:page_size],
        page_token: page_token
    )

    page_token = response.next_page_token

    unless response.finalized_deals.nil?
      response.finalized_deals.each do |finalized_deal|
        FinalizedDealPrinter.print(finalized_deal)
      end
    else
      puts 'No finalized deals found.'
    end
  end until page_token.nil?
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
      'The resource ID of the buyers resource for which the finalized deals are being retrieved. This will be used '\
      'to construct the parent used as a path parameter for the finalizedDeals.list request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'filter',
      'Query string to filter finalized deals. By default, this example will filter by deal type to retrieve '\
      'finalized deals including programmatic guaranteed deals to demonstrage usage.',
      type: Array, short_alias: 'f', required: false, default_value: 'deal.dealType = PROGRAMMATIC_GUARANTEED'
    ),
    Option.new(
      'order_by',
      'Query string used to sort the response of the list method. By default, this will return deals in descending '\
      'order of their flight start time to demonstrate usage. To learn more about the syntax for this parameter, '\
      'see: https://cloud.google.com/apis/design/design_patterns#sorting_order',
      type: Array, short_alias: 'f', required: false, default_value: 'deal.flightStartTime desc'
    ),
    Option.new(
      'page_size', 'The number of rows to return per page. The server may return fewer rows than specified.',
      type: Array, short_alias: 'p', required: false, default_value: MAX_PAGE_SIZE
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    list_finalized_deals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    puts "Error: #{e}"
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
