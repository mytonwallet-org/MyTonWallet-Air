//
//  NftCollectionFilter.swift
//  MyTonWalletAir
//
//  Created by nikstar on 03.07.2025.
//

import Foundation
import WalletContext
import OrderedCollections

public enum NftCollectionFilter: Equatable, Hashable, Codable, Sendable {
    case none
    case collection(NftCollection)
    case telegramGifts
}

extension NftCollectionFilter {
    public var displayTitle: String {
        switch self {
        case .none:
            "WStrings.Assets_Title.localized"
        case .collection(let nftCollection):
            nftCollection.name
        case .telegramGifts:
            "Telegram Gifts"
        }
    }
    
    public var stringValue: String {
        switch self {
        case .none:
            "nfts"
        case .collection(let nftCollection):
            nftCollection.address
        case .telegramGifts:
            "super:telegram-gifts"
        }
    }
    
    public func apply(to collection: OrderedDictionary<String, DisplayNft>) -> OrderedDictionary<String, DisplayNft> {
        switch self {
        case .none:
            collection
        case .collection(let nftCollection):
            collection.filter { $1.nft.collectionAddress == nftCollection.address }
        case .telegramGifts:
            collection.filter { $1.nft.collection?.isTelegramGiftsCollection == true }
        }
    }
}
