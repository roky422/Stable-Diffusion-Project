from torch import mode
from clip import CLIP
from encoder import VAE_Encoder
from decoder import VAE_Decoder
from diffusion import Diffusion

import model_converter1
import model_converter2

def preload_models_from_standard_weights(weights_path, device):
    model_path = " "
    print(weights_path)
    if "FN" in weights_path:
        if weights_path == "FN Naruto - 1 epoch":
            model_path = "C:/Users/babym/Documents/Coding/Stable_Diffusion_scratch_AI_project/data/diffusion_pytorch_model_FineTune(1epoch).safetensors"
        elif weights_path == "FN Naruto - 20 epoch":
            model_path = "C:/Users/babym/Documents/Coding/Stable_Diffusion_scratch_AI_project/data/diffusion_pytorch_model_FineTune(20epoch).safetensors"
        state_dict = model_converter2.load_from_standard_weights(model_path, device)
    else:
        model_path = "C:/Users/babym/Documents/Coding/Stable_Diffusion_scratch_AI_project/data/v1-5-pruned.safetensors"
        state_dict = model_converter1.load_from_standard_weights(model_path, device)

    encoder = VAE_Encoder().to(device)
    encoder.load_state_dict(state_dict['encoder'], strict=True)

    decoder = VAE_Decoder().to(device)
    decoder.load_state_dict(state_dict['decoder'], strict=True)

    diffusion = Diffusion().to(device)
    diffusion.load_state_dict(state_dict['diffusion'], strict=True)

    clip = CLIP().to(device)
    clip.load_state_dict(state_dict['clip'], strict=True)

    return {
        'clip': clip,
        'encoder': encoder,
        'decoder': decoder,
        'diffusion': diffusion,
    }