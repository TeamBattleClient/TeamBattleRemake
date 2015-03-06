package event.events;

import me.client.Client;
import me.client.command.Command;
import me.client.utils.Logger;
import event.Cancellable;
import event.Event;

public final class EventChatSent extends Event implements Cancellable {
	private boolean cancel;
	private String message;

	public EventChatSent(String message) {
		this.message = message;
	}

	public void checkForCommands() {
		if (message.startsWith(".")) {
			for (final Command command : Client.getCommandManager()
					.getContents()) {	
				if (message.split(" ")[0].equalsIgnoreCase("."
						+ command.getCommand())) {
					try {
						command.run(message);
					} catch (final Exception e) {
						Logger.logChat("Invalid arguments! "
								+ command.getCommand() + " "
								+ command.getArguments());
					}
					cancel = true;
				} else {
					for (final String alias : command.getAliases()) {
						if (message.split(" ")[0].equalsIgnoreCase("." + alias)) {
							try {
								command.run(message);
							} catch (final Exception e) {
								Logger.logChat("Invalid arguments! " + alias
										+ " " + command.getArguments());
							}
							cancel = true;
						}
					}
				}
			}

			if (!cancel) {
				Logger.logChat("Command \"" + message + "\" was not found!");
				cancel = true;
			}
		}
	}

	public String getMessage() {
		return message;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
