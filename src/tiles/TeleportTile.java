package tiles;

import creatures.CreatureFigure;
import start.Position;

public class TeleportTile extends Tile implements EnterTileEffect{

	public TeleportTile(Position position){
		super(position);
	}

	@Override
	public void landOn(CreatureFigure creature){
	}

	@Override
	public void render(){
	}

}
