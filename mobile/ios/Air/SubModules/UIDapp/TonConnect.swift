
import SwiftUI
import UIKit
import UIPasscode
import UIComponents
import WalletCore
import WalletContext


public let TonConnectErrorCodes: [Int: String] = [
    0: "unknownError",
    1: "badRequestError",
    2: "manifestNotFoundError",
    3: "manifestContentError",
    100: "unknownAppError",
    300: "userRejectsError",
    400: "methodNotSupported",
]


public final class TonConnect {
    
    public static let shared = TonConnect()
    
    private let log = Log("TonConnect")
    
    init() {
        WalletCoreData.add(eventObserver: self)
    }
    
    public func start() {
        // nothing to do, just makes sure shared TonConnect is initialized
    }
    
    public func handleDeeplink(_ url: String) {
        Task { @MainActor in
            do {
                let identifier = "\(Date().timeIntervalSince1970)"
                let returnStrategy = try await Api.startSseConnection(url: url, identifier: identifier)
                if let returnStrategy, case .url(var str) = returnStrategy {
                    if !str.contains("://") {
                        str = "https://" + str
                    }
                    if let url = URL(string: str) {
                        DispatchQueue.main.async {
                            UIApplication.shared.open(url)
                        }
                    }
                }
            } catch {
                log.error("failed to handle deeplink: \(error, .public)")
                topViewController()?.showAlert(error: error)
            }
        }
    }
    
    func presentConnect(request: MTonConnectRequest) {
        Task { @MainActor in
            let vc = ConnectDappVC(
                request: request,
                onConfirm: { [weak self] accountId, password in
                    self?.confirmConnect(request: request, accountId: accountId, passcode: password)
                },
                onCancel: { [weak self] in
                    self?.cancelConnect(request: request)
                })
            topViewController()?.present(vc, animated: true)
        }
    }
    
    func confirmConnect(request: MTonConnectRequest, accountId: String, passcode: String) {
        Task {
            do {
                try await Api.confirmDappRequest(
                    promiseId: request.promiseId,
                    password: passcode
                )
            } catch {
                log.error("confirmConnect \(error, .public)")
            }
        }
    }
    
    func cancelConnect(request: MTonConnectRequest) {
        Task {
            do {
                try await Api.cancelDappRequest(promiseId: request.promiseId, reason: "Cancel")
            } catch {
                log.error("cancelConnect \(error, .public)")
            }
        }
    }
    
    func presentSendTransactions(request: MDappSendTransactions) {
        Task { @MainActor in
            let vc = SendDappVC(
                request: request,
                onConfirm: { password in
                    if let password {
                        self.confirmSendTransactions(request: request, password: password)
                    } else {
                        self.cancelSendTransactions(request: request)
                    }
                }
            )
            let nc = WNavigationController(rootViewController: vc)
            if let sheet = nc.sheetPresentationController {
                sheet.detents = [.large()]
            }
            topViewController()?.present(nc, animated: true)
        }
    }
    
    func confirmSendTransactions(request: MDappSendTransactions, password: String) {
        Task {
            do {
                try await Api.confirmDappRequest(promiseId: request.promiseId, password: password)
            } catch {
                log.error("confirmSendTransactions \(error, .public)")
            }
        }
    }
    
    func cancelSendTransactions(request: MDappSendTransactions) {
        Task {
            do {
                try await Api.cancelDappRequest(promiseId: request.promiseId, reason: nil)
            } catch {
                log.error("cancelSendTransactions \(error, .public)")
            }
        }
    }
}


extension TonConnect: WalletCoreData.EventsObserver {
    public nonisolated func walletCore(event: WalletCore.WalletCoreData.Event) {
        switch event {
        case .dappConnect(request: let request):
            presentConnect(request: request)
        case .dappSendTransactions(let request):
            presentSendTransactions(request: request)
        default:
            break
        }
    }
}
